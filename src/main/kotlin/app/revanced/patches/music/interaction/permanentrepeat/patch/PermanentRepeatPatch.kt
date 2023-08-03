package app.revanced.patches.music.interaction.permanentrepeat.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.music.annotations.MusicCompatibility
import app.revanced.patches.music.interaction.permanentrepeat.fingerprints.PermanentRepeatFingerprint
import app.revanced.patcher.util.smali.ExternalLabel

@Patch(false)
@Name("Permanent repeat")
@Description("Permanently remember your repeating preference even if the playlist ends or another track is played.")
@MusicCompatibility
class PermanentRepeatPatch : BytecodePatch(
    listOf(PermanentRepeatFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        PermanentRepeatFingerprint.result?.let {
            val startIndex = it.scanResult.patternScanResult!!.endIndex;
            val repeatIndex = startIndex + 3;

            it.mutableMethod.also {
                it.addInstructionsWithLabels(
                    startIndex,
                    "goto :repeat",
                    ExternalLabel("repeat", it.getInstruction(repeatIndex))
                )
            }
        } ?: return PermanentRepeatFingerprint.toErrorResult()

        return PatchResultSuccess()
    }
}
