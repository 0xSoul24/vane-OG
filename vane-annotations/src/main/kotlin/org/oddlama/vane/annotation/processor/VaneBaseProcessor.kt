package org.oddlama.vane.annotation.processor

import javax.annotation.processing.AbstractProcessor
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic

abstract class VaneBaseProcessor : AbstractProcessor() {
    protected fun verifyIsClass(element: Element, annotationName: String) {
        if (element.kind != ElementKind.CLASS) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                element.asType().toString() + ": $annotationName must be applied to a class"
            )
        }
    }
}

