package it.unibo.coordination.linda.cli

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import it.unibo.coordination.linda.core.Tuple
import java.util.concurrent.CompletableFuture

abstract class AbstractUpdateCommand(
        help: String = "",
        epilog: String = "",
        name: String? = null,
        invokeWithoutSubcommand: Boolean = false,
        printHelpOnEmptyArgs: Boolean = false,
        helpTags: Map<String, String> = emptyMap(),
        autoCompleteEnvvar: String? = ""
) : AbstractTupleSpaceCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {

    val tuples: List<String> by argument("TUPLE").multiple(required = true)
    val asynchronous: Boolean by option("-A", "--asynch").flag(default = false)

    protected fun<T : Tuple> CompletableFuture<T>.defaultHandlerForSingleResult() {
        await {
            println("Success!")
            println("\tTuple $this has been inserted")
        }
    }

    protected fun<T : Tuple> CompletableFuture<T>.defaultAsyncHandlerForSingleResult(input: T) {
        println("Success!")
        println("\tTuple $input has been inserted")
    }

    protected fun<T : Tuple, C : Collection<out T>> CompletableFuture<C>.defaultHandlerForMultipleResult() {
        await {
            print("Success!")
            println("\tThe following tuples have been inserted:")
            forEachIndexed { i, t ->
                println("\t\t${i + 1}) $t")
            }
        }
    }

    protected fun<T : Tuple, C : Collection<out T>> CompletableFuture<C>.defaultAsyncHandlerForMultipleResult(input: Collection<out T>) {
        print("Success!")
        println("\tThe following tuples have been inserted:")
        input.forEachIndexed { i, t ->
            println("\t\t${i + 1}) $t")
        }
    }

}