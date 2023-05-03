package app.revanced.patches.hexeditor.ad.patch

import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.replaceInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.hexeditor.ad.annotations.HexEditorAdsCompatibility
import app.revanced.patches.hexeditor.ad.fingerprints.PrimaryAdsFingerprint

@Patch
@Name("disable-ads")
@Description("Disables ads in HexEditor.")
@HexEditorAdsCompatibility
@Version("0.0.1")
class HexEditorAdsPatch : BytecodePatch(
    listOf(
        PrimaryAdsFingerprint
    )
) {
    override fun execute(context: BytecodeContext) {
        val method = PrimaryAdsFingerprint.result!!.mutableMethod
        
        method.replaceInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """
        )

    }
}
