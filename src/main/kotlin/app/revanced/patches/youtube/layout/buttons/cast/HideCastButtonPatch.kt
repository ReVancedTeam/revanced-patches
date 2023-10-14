package app.revanced.patches.youtube.layout.buttons.cast

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.misc.integrations.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.SettingsPatch
import app.revanced.patches.youtube.misc.settings.SettingsResourcePatch

@Patch(
    name = "Hide cast button",
    description = "Hides the cast button in the video player.",
    dependencies = [
        IntegrationsPatch::class,
        SettingsPatch::class
    ],
    compatiblePackages = [
        CompatiblePackage("com.google.android.youtube")
    ]
)
object HideCastButtonPatch : BytecodePatch() {
    override fun execute(context: BytecodeContext) {
        SettingsResourcePatch.includePatchStrings("HideCastButton")
        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_cast_button",
                "revanced_hide_cast_button_title",
                "revanced_hide_cast_button_summary_on",
                "revanced_hide_cast_button_summary_off"
            )
        )

        val buttonClass = context.findClass("MediaRouteButton")
            ?: throw PatchException("MediaRouteButton class not found.")

        buttonClass.mutableClass.methods.find { it.name == "setVisibility" }?.apply {
            addInstructions(
                0,
                """
                    invoke-static {p1}, Lapp/revanced/integrations/patches/HideCastButtonPatch;->getCastButtonOverrideV2(I)I
                    move-result p1
                """
            )
        } ?: throw PatchException("setVisibility method not found.")
    }
}
