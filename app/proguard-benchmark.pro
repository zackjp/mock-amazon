# Optimize code, but don't scramble so we can see routes, Composables, and method names while profiling
-dontobfuscate

# TODO (6/16/26): Figure out why this rule makes profiling work.
# This should strip tracing from a release build, but contrary to the official Compose docs
# its inclusion seems to make the benchmarking actually function. Without it, the profiler
# doesn't even attach. Don't remove this line without first testing whether the profiler attaches.
# See – https://developer.android.com/develop/ui/compose/tooling/tracing.
-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
    boolean isTraceInProgress();
    void traceEventStart(int,int,int,java.lang.String);
    void traceEventEnd();
}
