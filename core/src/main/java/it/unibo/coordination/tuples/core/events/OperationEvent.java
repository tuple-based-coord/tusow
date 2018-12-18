package it.unibo.coordination.tuples.core.events;

import it.unibo.coordination.tuples.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OperationEvent<T extends Tuple, TT extends Template> extends TupleSpaceEvent<T, TT> {

    private final OperationType operationType;
    private final OperationPhase operationPhase;
    private final List<T> argumentTuples;
    private final List<TT> argumentTemplates;
    private final List<T> resultTuples;
    private final List<TT> resultTemplates;

    private OperationEvent(TupleSpace<T, TT> tupleSpace, OperationType operationType, OperationPhase operationPhase,
                           Stream<? extends T> argumentTuples, Stream<? extends TT> argumentTemplates, Stream<? extends T> resultTuples, Stream<? extends TT> resultTemplates) {
        super(tupleSpace);
        this.operationType = Objects.requireNonNull(operationType);
        this.operationPhase = Objects.requireNonNull(operationPhase);
        this.argumentTuples = argumentTuples.collect(Collectors.toList());
        this.argumentTemplates = argumentTemplates.collect(Collectors.toList());
        this.resultTuples = resultTuples.collect(Collectors.toList());
        this.resultTemplates = resultTemplates.collect(Collectors.toList());
    }

    public static <X extends Tuple, Y extends Template> Invocation<X, Y> nothingAcceptingInvocation(TupleSpace<X, Y> tupleSpace, OperationType operationType) {
        if (!OperationType.isNothingAccepting(operationType))
            throw new IllegalArgumentException(operationType.toString());

        return new Invocation<>(
                tupleSpace, operationType, Stream.empty(), Stream.empty()
        );
    }

    public static <X extends Tuple, Y extends Template> Invocation<X, Y> tupleAcceptingInvocation(TupleSpace<X, Y> tupleSpace, OperationType operationType, X tuple) {
        if (!OperationType.isTupleAcceptingSet(operationType))
            throw new IllegalArgumentException(operationType.toString());

        return new Invocation<>(
                tupleSpace, operationType, Stream.of(tuple), Stream.empty()
        );
    }

    public static <X extends Tuple, Y extends Template> Invocation<X, Y> tuplesAcceptingInvocation(TupleSpace<X, Y> tupleSpace, OperationType operationType, Collection<? extends X> tuples) {
        if (!OperationType.isTuplesAcceptingSet(operationType))
            throw new IllegalArgumentException(operationType.toString());

        return new Invocation<>(
                tupleSpace, operationType, tuples.stream(), Stream.empty()
        );
    }

    public static <X extends Tuple, Y extends Template> Invocation<X, Y> templateAcceptingInvocation(TupleSpace<X, Y> tupleSpace, OperationType operationType, Y template) {
        if (!OperationType.isTemplateAccepting(operationType))
            throw new IllegalArgumentException(operationType.toString());

        return new Invocation<>(
                tupleSpace, operationType, Stream.empty(), Stream.of(template)
        );
    }

    public static <X extends Tuple, Y extends Template> Invocation<X, Y> templatesAcceptingInvocation(TupleSpace<X, Y> tupleSpace, OperationType operationType, Collection<? extends Y> templates) {
        if (!OperationType.isTemplatesAccepting(operationType))
            throw new IllegalArgumentException(operationType.toString());

        return new Invocation<>(
                tupleSpace, operationType, Stream.empty(), templates.stream()
        );
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public OperationPhase getOperationPhase() {
        return operationPhase;
    }

    public Optional<T> getArgumentTuple() {
        return argumentTuples.stream().findFirst();
    }

    public Optional<TT> getArgumentTemplate() {
        return argumentTemplates.stream().findFirst();
    }

    public List<T> getArgumentTuples() {
        return List.copyOf(argumentTuples);
    }

    public List<TT> getArgumentTemplates() {
        return List.copyOf(argumentTemplates);
    }

    public boolean isArgumentPresent() {
        return argumentTuples.size() > 0 || argumentTemplates.size() > 0;
    }

    public Optional<T> getResultTuple() {
        return resultTuples.stream().findFirst();
    }

    public Optional<TT> getResultTemplate() {
        return resultTemplates.stream().findFirst();
    }

    public List<T> getResultTuples() {
        return List.copyOf(resultTuples);
    }

    public List<TT> getResultTemplates() {
        return List.copyOf(resultTemplates);
    }

    public boolean isResultPresent() {
        return resultTuples.size() > 0 || resultTemplates.size() > 0;
    }

    public static final class Invocation<T extends Tuple, TT extends Template> extends OperationEvent<T, TT> {

        private Invocation(TupleSpace<T, TT> tupleSpace, OperationType operationType, Stream<? extends T> argumentTuples, Stream<? extends TT> argumentTemplates) {
            super(tupleSpace, operationType, OperationPhase.INVOCATION, argumentTuples, argumentTemplates, Stream.empty(), Stream.empty());
        }

        public Completion<T, TT> toTupleReturningCompletion(T tuple) {
            if (!OperationType.isTemplateReturning(getOperationType()))
                throw new IllegalStateException();

            return new Completion<>(this, Stream.of(tuple), Stream.empty());
        }

        public Completion<T, TT> toTuplesReturningCompletion(Collection<? extends T> tuples) {
            if (!OperationType.isTuplesReturningSet(getOperationType()))
                throw new IllegalStateException();

            return new Completion<>(this, tuples.stream(), Stream.empty());
        }

        public Completion<T, TT> toTemplateReturningCompletion(TT template) {
            if (!OperationType.isTemplateReturning(getOperationType()))
                throw new IllegalStateException();

            return new Completion<>(this, Stream.empty(), Stream.of(template));
        }

        public Completion<T, TT> toTemplatesReturningCompletion(Collection<? extends TT> templates) {
            if (!OperationType.isTemplatesReturning(getOperationType()))
                throw new IllegalStateException();

            return new Completion<>(this, Stream.empty(), templates.stream());
        }
    }

    public static final class Completion<T extends Tuple, TT extends Template> extends OperationEvent<T, TT> {

        private Completion(Invocation<T, TT> invocation, Stream<? extends T> resultTuples, Stream<? extends TT> resultTemplates) {
            super(
                    invocation.getTupleSpace(),
                    invocation.getOperationType(),
                    OperationPhase.COMPLETION,
                    invocation.getResultTuples().stream(),
                    invocation.getArgumentTemplates().stream(),
                    resultTuples,
                    resultTemplates
            );
        }
    }
}
