import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.google.auto.service.AutoService
import org.objectweb.asm.tree.ClassNode

/**
 * Legacy skin transformer.
 *
 * The project is migrating away from bytecode-based activity superclass replacement
 * to explicit inheritance. Keep the transformer registered for now, but make it a no-op
 * so Booster no longer rewrites activity super classes during build.
 */
@AutoService(ClassTransformer::class)
class SkinApplicationTransformer : ClassTransformer {

    override fun onPreTransform(context: TransformContext) = Unit

    override fun onPostTransform(context: TransformContext) = Unit

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
        return klass
    }
}
