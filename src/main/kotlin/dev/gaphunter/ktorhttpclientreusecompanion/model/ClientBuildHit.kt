package dev.gaphunter.ktorhttpclientreusecompanion.model

import com.intellij.psi.PsiElement

/** One Ktor `HttpClient(...)` constructor call found inside a non-constructor function body. */
data class ClientBuildHit(val callElement: PsiElement)
