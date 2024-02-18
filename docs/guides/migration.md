---
{:title "Migration"
 :sequence 0.1
 :toc false
 :description "Immutant 1.1 -> 2.0 migration guide"}
---

This guide aims to ease the transition from Immutant 1.x to 2.x.

## General Changes

The biggest changes in 2.x are the ability to use Immutant libraries
embedded in a standard Clojure application, and the ability to deploy
an application to an unmodified [WildFly](http://wildfly.org)
container if you need container/cluster functionality.

For details, see the [Installation Guide](guide-installation.html), and
pay close attention to the way Immutant applications are now
initialized (via a standard `-main` function instead of the
Immutant-specific initialization process from 1.x). Also note that the
[lein-cloboss](https://github.com/cloboss/lein-cloboss) plugin is
now only needed if you need to
[create a WAR file to deploy to WildFly](guide-wildfly.html).


## API Changes

**Structure:** each section covers a namespace. If the namespace has been
renamed, that will be reflected by *old namespace -> new
namespace*. If a namespace has been removed, it's marked with a
~~strikethrough~~.

This list includes all of the Immutant 1.1 namespaces, some of which
were/are for internal use only.


### cloboss.cache -> [[cloboss.caching]]

The `Mutable` interface is gone. To put something in an Immutant
cache, you can use either the new `cloboss.caching/swap-in!`
or java interop (`org.infinispan.Cache` extends `ConcurrentMap` and
therefore `Map`). To insert entries with various ttl/idle values, use
the new `with-expiration` function.

Both `lookup` and `create` have been collapsed into `cache`, which
behaves like the old `lookup-or-create`. To force the creation of an
existing cache, you must `stop` the running one. Otherwise, `cache`
will just return it.

Caches are no longer transactional by default, and if set to be so,
require a locking mode to be selected (optimistic by default).

`core.cache` and `core.memoize` are no longer transitive dependencies
of `cloboss.caching`. To extend an cloboss cache to core's
`CacheProtocol`, add `org.clojure/core.cache` to your project's deps
and require `cloboss.caching.core-cache`.

The `memo` function has been moved to `cloboss.caching.core-memoize`
which you should only require after adding `org.clojure/core.memoize`
to your project's deps.

Some option keys and values have changed:

  - `:sync` has been collapsed into `:mode` to match
    `org.infinispan.configuration.cache/CacheMode`, so the possible
    values of `:mode` are now:
    - `:local`
    - `:repl-sync`
    - `:repl-async`
    - `:invalidation-sync`
    - `:invalidation-async`
    - `:dist-sync`
    - `:dist-async`
  - `:tx` -> `:transactional`
  - `:encoding` is gone, replaced with the `with-codec` fn
  - `:seed` is gone
  - `:config` is now `:configuration`

#### ~~cloboss.cache.config~~
#### ~~cloboss.cache.core~~
#### ~~cloboss.cache.wrapper~~

### [[cloboss.codecs]]

The `:text` codec was removed. The default supplied codecs in 2.x are:
`:none`, `:edn`, and `:json`. `:fressian` can be enabled by calling
`cloboss.codecs.fressian/register-fressian-codec`.

### [[cloboss.daemons]]

Now resides in
[org.cloboss/core](https://clojars.org/org.cloboss/core), with a
slightly simpler interface.

### ~~cloboss.dev~~

Used for dev inside the container, but you can get all these same
facilities with standard tools outside of the container with 2.x, and
we're no longer exposing the project map, so this wouldn't be very
useful inside the container with 2.x.

### cloboss.jobs -> [[cloboss.scheduling]]

The API is similar. `schedule` now takes a map or kwargs, and there
are now helpers for each option that help you generate that map. A
cronspec is no longer a top-level arg, but instead is specified in the
map using the `:cron` key. A name is no longer required, but an
optional id can be provided to allow you to reschedule the job.

The `set-scheduler-options` is now handled by passing additional
options to `schedule`. If different scheduler options are given on
different schedule calls, new schedulers are created.

If you need access to the raw quartz scheduler, use
[[cloboss.scheduling.quartz/quartz-scheduler]].

#### cloboss.jobs.internal -> cloboss.scheduling.internal

### ~~cloboss.logging~~

### [[cloboss.messaging]]

Has a similar API, except around destination creation and passing. Fns
now take destination objects instead of strings, and the destination
objects must be created via `queue` and `topic`. Connections and
sessions have been replaced with contexts, available from `context`.

`unlisten` and `stop` have been merged in to `stop`. `message-seq` is
no more.

#### ~~cloboss.messaging.codecs~~

#### ~~cloboss.messaging.core~~

Merged with `cloboss.messaging.internal`.

#### [[cloboss.messaging.hornetq]]

Brought over with a few changes.

#### cloboss.messaging.internal

Brought over, but with a drastically different API.

### cloboss.pipeline -> [[cloboss.messaging.pipeline]]

The API is unchanged, other than renaming the namespace.

### ~~cloboss.registry~~

The registry was used to store application config and the parsed lein
project map. We no longer provide those inside Immutant, so the
registry is no longer needed for that. You could also use the registry
to look up items in the application server's internal registry. That
functionality is still available via
[[cloboss.wildfly/get-from-service-registry]].

### cloboss.repl -> cloboss.wildfly.repl

Still there, but with a different API. It's now only used inside the
container.

### ~~cloboss.resource-util~~

### ~~cloboss.runtime~~

#### ~~cloboss.runtime.bootstrap~~

### ~~cloboss.runtime-util~~

### cloboss.util

Split across three namespaces:

* [[cloboss.util]] - fns appropriate for app use
* cloboss.internal.util - fns used by Immutant itself, and not intended for app use
* [[cloboss.wildfly]] - in-container specific functions

### [[cloboss.web]]

* `start` is now `run`
* `start-servlet` is also now `run`
* `current-servlet-request` currently has no analogue

#### ~~cloboss.web.session~~

Obviated by [[cloboss.web.middleware/wrap-session]].

#### ~~cloboss.web.servlet~~
#### ~~cloboss.web.session.internal~~

#### [[cloboss.web.middleware]]

Contains only `wrap-development`, `wrap-session`, and `wrap-websocket`.

### cloboss.xa -> [[cloboss.transactions]]

Listeners are no longer automatically enlisted participants in an XA
transaction. Within the handler fn, you must now explicitly define a
transaction using one of the macros in [[cloboss.transactions]]. If an
exception escapes that body, the tx will be rolled back, and if the
exception bubbles out of the handler, the message will be queued for
redelivery. But the rollback of the tx has no relationship to
redelivery, which is only triggered by the exception.

Messaging connections, now called contexts, are no longer XA by
default, so you must set the :xa? option to true when you create your
own contexts to pass to the messaging functions. If you don't pass
your own contexts, those functions will create an XA capable context
only if within an active transaction as defined by the scoping macros.

The `cloboss.xa/datasource` function has been removed, as it would be
impractical to support it outside of the app server. It's still
possible to include SQL datasources within an XA transaction inside
WildFly by configuring them there and referring to them by JNDI name.
It is also possible to manipulate a non-XA SQL datasource within an XA
transaction as long as 1) it's the only non-XA resource and 2) it is
operated on last. This "trick" relies on an exception being thrown if
the datasource operation fails, hence causing the other XA
participants, e.g. messaging destinations or caches, to roll back.

### cloboss.xa.transaction -> cloboss.transactions.scope

All the scope macros, analogous to the JEE Transaction attribute
annotations, have been moved to [[cloboss.transactions.scope]]. The
[[cloboss.transactions/transaction]] macro is an alias for
[[cloboss.transactions.scope/required]], just as in Immutant 1.x.
