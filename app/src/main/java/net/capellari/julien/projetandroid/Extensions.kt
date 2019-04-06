package net.capellari.julien.projetandroid

import com.github.kittinunf.result.Result
import net.capellari.julien.utils.SuccessFail
import java.lang.Exception

inline fun <reified V: Any, reified E: Exception> Result<V,E>.fold(cbs: SuccessFail<V,E>)
        = fold(cbs._success, cbs._fail)