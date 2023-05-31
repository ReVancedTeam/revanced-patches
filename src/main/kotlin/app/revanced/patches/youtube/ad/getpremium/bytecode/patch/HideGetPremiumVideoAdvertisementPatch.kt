package app.revanced.patches.youtube.ad.getpremium.bytecode.patch

import app.revanced.extensions.error
import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patches.shared.settings.preference.impl.StringResource
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.ad.getpremium.annotations.HideGetPremiumCompatibility
import app.revanced.patches.youtube.ad.getpremium.bytecode.fingerprints.GetPremiumViewFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.SettingsPatch
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction

@DependsOn([IntegrationsPatch::class, SettingsPatch::class])
@Name("hide-get-premium")
@HideGetPremiumCompatibility
@Version("0.0.1")
class HideGetPremiumPatch : BytecodePatch(listOf(GetPremiumViewFingerprint,)) {
    override suspend fun execute(context: BytecodeContext) {
        SettingsPatch.PreferenceScreen.ADS.addPreferences(
            SwitchPreference(
                "revanced_hide_get_premium",
                StringResource(
                    "revanced_hide_get_premium_title",
                    "Hide YouTube Premium advertisement under video player"
                ),
                StringResource(
                    "revanced_hide_get_premium_summary_on",
                    "YouTube Premium advertisement are hidden"
                ),
                StringResource(
                    "revanced_hide_get_premium_summary_off",
                    "YouTube Premium advertisement are shown"
                )
            )
        )

        GetPremiumViewFingerprint.result?.let {
            it.mutableMethod.apply {
                val startIndex = it.scanResult.patternScanResult!!.startIndex
                val measuredWidthRegister = instruction<TwoRegisterInstruction>(startIndex).registerA
                val measuredHeightInstruction = instruction<TwoRegisterInstruction>(startIndex + 1)

                val measuredHeightRegister = measuredHeightInstruction.registerA
                val tempRegister = measuredHeightInstruction.registerB

                addInstructions(
                    startIndex + 2,
                    """
                        # Override the internal measurement of the layout with zero values.
                        invoke-static {}, $INTEGRATIONS_CLASS_DESCRIPTOR->hideGetPremiumView()Z
                        move-result v$tempRegister
                        if-eqz v$tempRegister, :allow
                        const/4 v$measuredWidthRegister, 0x0
                        const/4 v$measuredHeightRegister, 0x0
                        :allow
                        nop
                        # Layout width/height is then passed to a protected class method.
                    """
                )
            }
        } ?: GetPremiumViewFingerprint.error()
    }

    private companion object {
        const val INTEGRATIONS_CLASS_DESCRIPTOR =
            "Lapp/revanced/integrations/patches/HideGetPremiumPatch;"
    }
}
