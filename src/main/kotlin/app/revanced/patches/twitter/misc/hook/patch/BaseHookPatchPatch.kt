package app.revanced.patches.twitter.misc.hook.patch

import app.revanced.patcher.BytecodeContext
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patches.twitter.misc.hook.json.patch.JsonHookPatch

abstract class BaseHookPatchPatch(private val hookClassDescriptor: String) : BytecodePatch() {
    override suspend fun execute(context: BytecodeContext) =
        JsonHookPatch.hooks.addHook(JsonHookPatch.Hook(context, hookClassDescriptor))
}