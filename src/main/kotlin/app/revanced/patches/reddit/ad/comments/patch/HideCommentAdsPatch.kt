package app.revanced.patches.reddit.ad.comments.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.annotations.RequiresIntegrations
import app.revanced.patches.reddit.ad.comments.annotations.HideCommentAdsCompatibility
import app.revanced.patches.reddit.ad.comments.fingerprints.HideCommentAdsFingerprint

@Patch
@Name("hide-comment-ads")
@Description("Removes all comment ads.")
@HideCommentAdsCompatibility
@RequiresIntegrations
@Version("0.0.1")
class HideCommentAdsPatch : BytecodePatch(
    listOf(HideCommentAdsFingerprint)
) {
    override fun execute(context: BytecodeContext): PatchResult {
        val method = HideCommentAdsFingerprint.result!!.mutableMethod
        // Throws an exception when loading the comments page ad.
        // Something more elegant would be nice.
        method.addInstructions(
            0,
            """
            const-string v0, "Error loading comments page ad"
            new-instance v0, Ljava/lang/RuntimeException;
            invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V
            throw v0
            """
        )
        return PatchResultSuccess()
    }
}
