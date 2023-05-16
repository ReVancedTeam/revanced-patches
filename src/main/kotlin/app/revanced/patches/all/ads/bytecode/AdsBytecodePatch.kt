package app.revanced.patches.all.ads.bytecode

import app.revanced.patcher.annotation.*
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.util.bytecode.*
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.instruction.formats.Instruction35c
import org.jf.dexlib2.iface.reference.MethodReference
import org.jf.dexlib2.iface.reference.StringReference
import org.jf.dexlib2.immutable.reference.ImmutableStringReference
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x
import org.jf.dexlib2.iface.instruction.Instruction

@Patch(false)
@Name("remove-ads")
@Description("Attempts to remove ads.")
@Version("0.0.1")
class RemoveAdsPatch : BytecodePatch() {
    override fun execute(context: BytecodeContext): PatchResult {
        BytecodeUtils.transformIns(context, ::invokesTransformFunction, invokeOpcodes)
        BytecodeUtils.transformIns(context, ::urlTransformFunction, stringOpcodes)

        return PatchResultSuccess()
    }

    private fun invokesTransformFunction(ins: Instruction, index: Int, methodDef: Method, classDef: ClassDef, context: BytecodeContext) {
        if (ins is Instruction35c) {
            val instructionMethodReference = ins.getReference() as MethodReference

            if (blockInvokes.any{ it == instructionMethodReference.getName() }) {
                // make class and method mutable, if not already
                var mutableMethod = BytecodeUtils.makeMethodMutable(context, classDef, methodDef)

                mutableMethod!!.implementation!!.replaceInstruction(
                    index,
                    BuilderInstruction10x(
                        Opcode.NOP
                    )
                )
            } 
        }
    }

    private fun urlTransformFunction(ins: Instruction, index: Int, methodDef: Method, classDef: ClassDef, context: BytecodeContext) {
        if (ins is Instruction21c) {
            val str = (ins.reference as StringReference).string
            if (blockUrls.any{ it == str }) {
                // make class and method mutable, if not already
                var mutableMethod = BytecodeUtils.makeMethodMutable(context, classDef, methodDef)

                // replace instruction with updated string
                mutableMethod!!.implementation!!.replaceInstruction(
                    index,
                    BuilderInstruction21c(
                        Opcode.CONST_STRING,
                        ins.registerA,
                        ImmutableStringReference(
                            replaceUrlsWith
                        )
                    )
                )
            } 
        }
    }
}
