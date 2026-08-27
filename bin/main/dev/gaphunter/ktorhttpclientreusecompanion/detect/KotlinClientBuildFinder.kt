package dev.gaphunter.ktorhttpclientreusecompanion.detect

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.ktorhttpclientreusecompanion.model.ClientBuildHit
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstructor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/**
 * Finds `HttpClient(...)` constructor calls (Ktor client, Kotlin-only
 * -- there is no Java equivalent) written inside a non-constructor
 * function body -- Ktor's own documentation states plainly: "creating
 * HttpClient is not a cheap operation, and it's better to reuse its
 * instance in the case of multiple requests." Building one inside a
 * regular function means a brand new client (and its own connection
 * pool/engine) gets created on every call.
 *
 * **v0.1 scope, stated honestly:** only flags a direct `HttpClient(...)`
 * call (with or without a trailing config lambda) -- matches by simple
 * callee name, so it works whether the real Ktor client jar is on the
 * classpath or not. Never flags a call inside a constructor, a
 * top-level/class property initializer, or a companion object
 * (all legitimate "create once" locations).
 */
object KotlinClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitCallExpression(expression: KtCallExpression) {
                super.visitCallExpression(expression)
                hitFor(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitFor(call: KtCallExpression): ClientBuildHit? {
        if (call.calleeExpression?.text != "HttpClient") return null

        // Not inside a constructor -- a legitimate "create once" location.
        if (PsiTreeUtil.getParentOfType(call, KtConstructor::class.java) != null) return null

        // A property initializer (`val client = HttpClient(...)`) is a
        // legitimate "create once" location UNLESS that property is a
        // local variable declared inside a function body -- a local
        // `val` re-created every call is exactly the same problem as a
        // bare expression.
        val enclosingProperty = PsiTreeUtil.getParentOfType(call, KtProperty::class.java)
        if (enclosingProperty != null && !enclosingProperty.isLocal) return null

        // Must be inside some real function body to be flagged at all --
        // a call sitting directly in a class/object body outside any
        // function isn't a per-call construction pattern.
        PsiTreeUtil.getParentOfType(call, KtNamedFunction::class.java) ?: return null

        return ClientBuildHit(leafOf(call))
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
