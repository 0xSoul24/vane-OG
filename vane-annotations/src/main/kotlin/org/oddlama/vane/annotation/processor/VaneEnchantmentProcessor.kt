package org.oddlama.vane.annotation.processor

import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

@SupportedAnnotationTypes("org.oddlama.vane.annotation.enchantment.VaneEnchantment")
@SupportedSourceVersion(
    SourceVersion.RELEASE_21
)
class VaneEnchantmentProcessor : VaneBaseProcessor() {
    override fun process(annotations: MutableSet<out TypeElement?>, roundEnv: RoundEnvironment): Boolean {
        for (annotation in annotations) {
            roundEnv.getElementsAnnotatedWith(annotation).forEach { element: Element -> this.verifyIsClass(element, "@VaneEnchantment") }
            roundEnv.getElementsAnnotatedWith(annotation)
                .forEach { element: Element? -> this.verifyExtendsModule(element) }
        }

        return true
    }

    private fun verifyExtendsModule(element: Element?) {
        var t = element as TypeElement
        while (true) {
            if (t.asType().toString().startsWith("org.oddlama.vane.core.enchantments.CustomEnchantment<")) {
                return
            }
            if (t.superclass is DeclaredType) {
                t = (t.superclass as DeclaredType).asElement() as TypeElement
            } else {
                break
            }
        }

        processingEnv
            .messager
            .printMessage(
                Diagnostic.Kind.ERROR,
                element.asType().toString() +
                        ": @VaneEnchantment must be applied to a class inheriting from org.oddlama.vane.core.enchantments.CustomEnchantment"
            )
    }
}
