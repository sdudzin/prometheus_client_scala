# Introduction to Prometheus Scala client

## Creating and finding monitoring variables

This scala client makes extensive use of the scala type system when
creating monitoring collectors. Every collector has a distinctive
type, based on the collector name, and any labels attached.

You can therefore use the implicit scope to store and lookup
monitoring collectors.

### Creating a collector

Here is an example where a simple counter is created:

```tut
import io.prometheus.client.scala._

implicit val totalRequests = Counter.create("total_requests")
```

Note that the counter is a `Counter0`, which means that it
has no labels. Therefore, it needs no corresponding label values
when incrementing the counter.

### Finding the collector

You can find this counter in the implicit scope like this:

```tut
Counter.lookup("total_requests").inc
```

If the variable is not currently available, this would be a
compilation error:

```tut:fail
Counter.lookup("no_such_variable").inc
```

You can always use the counter variable directly, though
there is no performance benefit in this (it could make your
code clearer in some cases):

```tut
totalRequests.inc
```

### Creating collectors with labels

Any extra strings passed when creating a collector, represent
labels for any monitoring variables. Whenever any information is
passed to the monitoring system, an appropriate number of label
values need to be provided; one for each label.

```tut
implicit val totalErrors = Counter.create("total_errors", "code")
```

### Using collectors with labels

The types of the collectors include the label names, so we can
look these up in implicit scope again.

To increment a counter with an error code of "404", one might
do the following:

```tut
Counter.lookup("total_errors", "code").inc("404")
```