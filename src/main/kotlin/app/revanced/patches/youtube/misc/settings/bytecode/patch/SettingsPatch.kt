package app.revanced.patches.youtube.misc.settings.bytecode.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.extensions.replaceInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.settings.preference.impl.Preference
import app.revanced.patches.shared.settings.util.AbstractPreferenceScreen
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.fingerprints.LicenseActivityFingerprint
import app.revanced.patches.youtube.misc.settings.bytecode.fingerprints.SetThemeFingerprint
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.util.MethodUtil

@DependsOn([IntegrationsPatch::class, SettingsResourcePatch::class])
@Name("settings")
@Description("Adds settings for ReVanced to YouTube.")
@Version("0.0.1")
class SettingsPatch : BytecodePatch(
    listOf(LicenseActivityFingerprint, SetThemeFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        // TODO: Remove this when it is only required at one place.
        fun getSetThemeInstructionString(
            registers: String = "v0",
            classDescriptor: String = THEME_HELPER_DESCRIPTOR,
            methodName: String = SET_THEME_METHOD_NAME,
            parameters: String = "Ljava/lang/Object;"
        ) = "invoke-static { $registers }, $classDescriptor->$methodName($parameters)V"

        SetThemeFingerprint.result?.mutableMethod?.let { setThemeMethod ->
            setThemeMethod.implementation!!.instructions.mapIndexedNotNull { i, instruction ->
                    if (instruction.opcode == Opcode.RETURN_OBJECT) i else null
                }
                .asReversed() // Prevent index shifting.
                .forEach { returnIndex ->
                    // The following strategy is to replace the return instruction with the setTheme instruction,
                    // then add a return instruction after the setTheme instruction.
                    // This is done because the return instruction is a target of another instruction.

                    setThemeMethod.apply {
                        // This register is returned by the setTheme method.
                        val register = instruction<OneRegisterInstruction>(returnIndex).registerA

                        val setThemeInstruction = getSetThemeInstructionString("v$register")
                        replaceInstruction(returnIndex, setThemeInstruction)
                        addInstruction(returnIndex + 1, "return-object v0")
                    }
                }
        } ?: return SetThemeFingerprint.toErrorResult()


        // Modify the license activity and remove all existing layout code.
        // Must modify an existing activity and cannot add a new activity to the manifest,
        // as that fails for root installations.
        LicenseActivityFingerprint.result!!.apply licenseActivity@{
            mutableMethod.apply {
                fun buildSettingsActivityInvokeString(
                    registers: String = "p0",
                    classDescriptor: String = SETTINGS_ACTIVITY_DESCRIPTOR,
                    methodName: String = "initializeSettings",
                    parameters: String = "Landroid/app/Activity;"
                ) = getSetThemeInstructionString(registers, classDescriptor, methodName, parameters)

                // initialize the settings
                addInstructions(
                    1, """
                        ${buildSettingsActivityInvokeString()}
                        return-void
                    """
                )
            }

            // remove method overrides
            mutableClass.apply {
                methods.removeIf { it.name != "onCreate" && !MethodUtil.isConstructor(it) }
            }
        }


        return PatchResultSuccess()
    }

    internal companion object {
        private const val INTEGRATIONS_PACKAGE = "app/revanced/integrations"
        private const val SETTINGS_ACTIVITY_DESCRIPTOR = "L$INTEGRATIONS_PACKAGE/settingsmenu/ReVancedSettingActivity;"
        private const val THEME_HELPER_DESCRIPTOR = "L$INTEGRATIONS_PACKAGE/utils/ThemeHelper;"
        private const val SET_THEME_METHOD_NAME = "setTheme"

        fun addPreferenceScreen(preferenceScreen: app.revanced.patches.shared.settings.preference.impl.PreferenceScreen) =
            SettingsResourcePatch.addPreferenceScreen(preferenceScreen)

        fun addPreference(preference: Preference) = SettingsResourcePatch.addPreference(preference)

        fun renameIntentsTargetPackage(newPackage: String) {
            SettingsResourcePatch.overrideIntentsTargetPackage = newPackage
        }

        /**
         * Creates an intent to open ReVanced settings of the given name
         */
        fun createReVancedSettingsIntent(settingsName: String) = Preference.Intent(
            "com.google.android.youtube",
            settingsName,
            "com.google.android.libraries.social.licenses.LicenseActivity"
        )
    }

    /**
     * Preference screens patches should add their settings to.
     */
    internal object PreferenceScreen : AbstractPreferenceScreen() {
        val ADS = Screen("revanced_ads_screen", "revanced_ads_screen_title", "revanced_ads_screen_summary")
        val INTERACTIONS = Screen("revanced_interaction_screen", "revanced_interaction_screen_title", "revanced_interaction_screen_summary")
        val LAYOUT = Screen("revanced_layout_screen", "revanced_layout_screen_title", "revanced_layout_screen_summary")
        val MISC = Screen("revanced_misc_screen", "revanced_misc_screen_title", "revanced_misc_screen_summary")
        val VIDEO = Screen("revanced_video_screen", "revanced_video_screen_title", "revanced_video_screen_summary")

        override fun commit(screen: app.revanced.patches.shared.settings.preference.impl.PreferenceScreen) {
            addPreferenceScreen(screen)
        }
    }

    override fun close() = PreferenceScreen.close()
}
