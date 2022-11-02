package app.revanced.patches.youtube.layout.comments.bytecode.fingerprints

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.youtube.layout.comments.annotations.CommentsCompatibility
import app.revanced.patches.youtube.layout.comments.resource.patch.CommentsResourcePatch
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction

@Name("live-chat-fullscreen-button-fingerprint")
@CommentsCompatibility
@Version("0.0.1")
object LiveChatFullscreenResourceFingerprint : MethodFingerprint(
    customFingerprint = {methodDef ->
        methodDef.implementation?.instructions?.any {
            it.opcode.ordinal == Opcode.CONST.ordinal &&
            (it as WideLiteralInstruction).wideLiteral == CommentsResourcePatch.liveChatButtonId
        } == true
    }
)