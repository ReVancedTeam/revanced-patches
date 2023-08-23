package app.revanced.patches.youtube.misc.links.open.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.links.open.annotations.OpenLinksExternallyCompatibility
import app.revanced.patches.youtube.misc.links.open.fingerprints.BindSessionServiceFingerprint
import app.revanced.patches.youtube.misc.links.open.fingerprints.GetCustomTabPackageNameFingerprint
import app.revanced.patches.youtube.misc.links.open.fingerprints.InitializeCustomTabSupportFingerprint
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c

@Patch
@Name("Open links externally")
@Description("Open links outside of the app directly in your browser.")
@OpenLinksExternallyCompatibility
class OpenLinksExternallyPatch : BytecodePatch(
    listOf(
        GetCustomTabPackageNameFingerprint,
        BindSessionServiceFingerprint,
        InitializeCustomTabSupportFingerprint
    )
) {
    override fun execute(context: BytecodeContext) {
        YouTubeSettingsPatch.PreferenceScreen.MISC.addPreferences(
            SwitchPreference(
                "revanced_external_browser",
                "revanced_external_browser_title",
                "revanced_external_browser_summary_on",
                "revanced_external_browser_summary_off"
            )
        )

        arrayOf(
            GetCustomTabPackageNameFingerprint,
            BindSessionServiceFingerprint,
            InitializeCustomTabSupportFingerprint
        ).forEach {
            val result = it.result ?: throw it.toErrorResult()
            val insertIndex = result.scanResult.patternScanResult!!.endIndex + 1
            with(result.mutableMethod) {
                val register = (implementation!!.instructions[insertIndex - 1] as Instruction21c).registerA
                addInstructions(
                    insertIndex,
                    """
                        invoke-static {v$register}, Lapp/revanced/integrations/patches/OpenLinksExternallyPatch;->enableExternalBrowser(Ljava/lang/String;)Ljava/lang/String;
                        move-result-object v$register
                    """
                )
            }
        }
    }
}