package app.revanced.patches.reddit.ad.comments.patch

import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.reddit.ad.comments.fingerprints.HideCommentAdsFingerprint

@Name("Hide comment ads")
@Description("Removes all comment ads.")
@Version("0.0.1")
class HideCommentAdsPatch : BytecodePatch(
    listOf(HideCommentAdsFingerprint)
) {
    override suspend fun execute(context: BytecodeContext) {
        val method = HideCommentAdsFingerprint.result!!.mutableMethod
        // Returns a blank object instead of the comment ad.
        method.addInstructions(
            0,
            """
            new-instance v0, Ljava/lang/Object;
            invoke-direct {v0}, Ljava/lang/Object;-><init>()V
            return-object v0
            """
        )
    }
}
