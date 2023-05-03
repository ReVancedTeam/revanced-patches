package app.revanced.patches.tiktok.misc.login.fixgoogle.patch

import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.tiktok.misc.login.fixgoogle.annotations.FixGoogleLoginCompatibility
import app.revanced.patches.tiktok.misc.login.fixgoogle.fingerprints.GoogleAuthAvailableFingerprint
import app.revanced.patches.tiktok.misc.login.fixgoogle.fingerprints.GoogleOneTapAuthAvailableFingerprint

@Patch
@Name("fix-google-login")
@Description("Allows logging in with a Google account.")
@FixGoogleLoginCompatibility
@Version("0.0.1")
class FixGoogleLoginPatch : BytecodePatch(
    listOf(
        GoogleOneTapAuthAvailableFingerprint,
        GoogleAuthAvailableFingerprint
    )
) {
    override fun execute(context: BytecodeContext) {
        listOf(
            GoogleOneTapAuthAvailableFingerprint,
            GoogleAuthAvailableFingerprint
        ).forEach {
            with(it.result!!.mutableMethod) {
                addInstructions(
                    0,
                    """
                        const/4 v0, 0x0
                        return v0
                    """
                )
            }
        }
        return PatchResult.Success
    }
}