package app.revanced.patches.youtube.layout.buttons.player.hide.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.shared.settings.preference.impl.SwitchPreference
import app.revanced.patches.youtube.layout.buttons.player.hide.annotations.HidePlayerButtonsCompatibility
import app.revanced.patches.youtube.layout.buttons.player.hide.fingerprints.PlayerControlsVisibilityModelFingerprint
import app.revanced.patches.youtube.misc.integrations.patch.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.bytecode.patch.YouTubeSettingsPatch
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc

@Patch
@DependsOn([IntegrationsPatch::class, YouTubeSettingsPatch::class])
@Name("Hide player buttons")
@Description("Adds the option to hide video player previous and next buttons.")
@HidePlayerButtonsCompatibility
class HidePlayerButtonsPatch : BytecodePatch(
    listOf(PlayerControlsVisibilityModelFingerprint)
) {
    private object ParameterOffsets {
        const val HAS_NEXT = 5
        const val HAS_PREVIOUS = 6
    }

    override fun execute(context: BytecodeContext) {
        YouTubeSettingsPatch.PreferenceScreen.LAYOUT.addPreferences(
            SwitchPreference(
                "revanced_hide_player_buttons",
                "revanced_hide_player_buttons_title",
                "revanced_hide_player_buttons_summary_on",
                "revanced_hide_player_buttons_summary_off"
            )
        )

        PlayerControlsVisibilityModelFingerprint.result?.apply {
            val callIndex = scanResult.patternScanResult!!.endIndex
            val callInstruction = mutableMethod.getInstruction<Instruction3rc>(callIndex)

            // overriding this parameter register hides the previous and next buttons
            val hasNextParameterRegister = callInstruction.startRegister + ParameterOffsets.HAS_NEXT
            val hasPreviousParameterRegister = callInstruction.startRegister + ParameterOffsets.HAS_PREVIOUS

            mutableMethod.addInstructions(
                callIndex,
                """
                    invoke-static { v$hasNextParameterRegister }, Lapp/revanced/integrations/patches/HidePlayerButtonsPatch;->previousOrNextButtonIsVisible(Z)Z
                    move-result v$hasNextParameterRegister
                    
                    invoke-static { v$hasPreviousParameterRegister }, Lapp/revanced/integrations/patches/HidePlayerButtonsPatch;->previousOrNextButtonIsVisible(Z)Z
                    move-result v$hasPreviousParameterRegister
                """
            )
        } ?: throw PlayerControlsVisibilityModelFingerprint.toErrorResult()
    }
}
