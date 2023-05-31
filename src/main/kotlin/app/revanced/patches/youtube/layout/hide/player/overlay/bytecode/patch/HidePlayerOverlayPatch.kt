package app.revanced.patches.youtube.layout.hide.player.overlay.bytecode.patch

import app.revanced.extensions.error
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.instruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.youtube.layout.hide.player.overlay.annotations.HidePlayerOverlayPatchCompatibility
import app.revanced.patches.youtube.layout.hide.player.overlay.bytecode.fingerprints.CreatePlayerOverviewFingerprint
import app.revanced.patches.youtube.layout.hide.player.overlay.resource.patch.HidePlayerOverlayResourcePatch
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction

@Patch
@Name("hide-player-overlay")
@Description("Hides the dark background overlay from the player when player controls are visible.")
@DependsOn([HidePlayerOverlayResourcePatch::class])
@HidePlayerOverlayPatchCompatibility
@Version("0.0.2")
class HidePlayerOverlayPatch : BytecodePatch(listOf(CreatePlayerOverviewFingerprint)) {
    override suspend fun execute(context: BytecodeContext) {
        CreatePlayerOverviewFingerprint.result?.let { result ->
            result.mutableMethod.apply {
                val viewRegisterIndex = implementation!!.instructions.indexOfFirst {
                    val literal = (it as? WideLiteralInstruction)?.wideLiteral

                    literal == HidePlayerOverlayResourcePatch.scrimOverlayId
                } + 3
                val viewRegister = instruction<OneRegisterInstruction>(viewRegisterIndex).registerA

                val insertIndex = viewRegisterIndex + 1
                addInstruction(
                    insertIndex,
                    "invoke-static { v$viewRegister }, " +
                            "$INTEGRATIONS_CLASS_DESCRIPTOR->hidePlayerOverlay(Landroid/widget/ImageView;)V"
                )
            }
        } ?: CreatePlayerOverviewFingerprint.error()
    }

    private companion object {
        const val INTEGRATIONS_CLASS_DESCRIPTOR = "Lapp/revanced/integrations/patches/HidePlayerOverlayPatch;"
    }
}
