import com.didiglobal.booster.kotlinx.touch
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.google.auto.service.AutoService
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.PrintWriter


/**
 * Author : long
 * Description : 替换基类为换肤的基类
 * Date : 2022/1/29
 */
@AutoService(ClassTransformer::class)
class SkinApplicationTransformer : ClassTransformer{

    private lateinit var logger: PrintWriter

    companion object{
        val DEST_CLASS_NAME = "com/hjl/lwanandroid/skin/SkinBaseActivity"
        val targetClsList = arrayListOf(
            "com/hjl/commonlib/base/BaseMultipleActivity",
            "com/hjl/jetpacklib/mvvm/view/BaseActivity",
//        "androidx/appcompat/app/AppCompatActivity"
        )
        val targetMthList = arrayListOf("getDelegate","onResume","onDestroy")
    }



    override fun onPreTransform(context: TransformContext) {
        super.onPreTransform(context)
        println("SkinApplicationTransformer onPreTransform")
        this.logger = getReport(context, "report.txt").touch().printWriter()
    }

    override fun onPostTransform(context: TransformContext) {
        super.onPostTransform(context)
//        println("SkinApplicationTransformer onPostTransform")
        this.logger.close()
    }

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
//        println("SkinApplicationTransformer transform")
        if (targetClsList.contains(klass.name)){
            println("get klass name:${klass.name}")
            klass.superName = DEST_CLASS_NAME

            klass.methods.filter {
                return@filter targetMthList.contains(it.name)
            }.forEach { mthNode ->

//
                mthNode.instructions.iterator().forEach {
                    if (it is org.objectweb.asm.tree.MethodInsnNode
                        && it.opcode == Opcodes.INVOKESPECIAL
                        && it.name == mthNode.name
                        && it.owner == "androidx/appcompat/app/AppCompatActivity"
                    ){
                        println("${mthNode.name} get insn : ${it.name } , ${it.owner}")
                        it.owner = DEST_CLASS_NAME
                    }

                }

            }

            return klass
        }


        return super.transform(context, klass)
    }




}