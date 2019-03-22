/*
 Copyright (C) 2015 - 2019 Electronic Arts Inc.  All rights reserved.
 This file is part of the Orbit Project <https://www.orbit.cloud>.
 See license in LICENSE.
 */

package cloud.orbit.runtime.test

import cloud.orbit.common.exception.ResponseTimeoutException
import cloud.orbit.common.time.TimeMs
import cloud.orbit.core.actor.ActorWithNoKey
import cloud.orbit.core.actor.getReference
import cloud.orbit.runtime.util.StageBaseTest
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

interface BasicTestActorInterface : ActorWithNoKey {
    fun echo(msg: String): Deferred<String>
    fun waitFor(Long: TimeMs): Deferred<Unit>
}

class BasicTestActorImpl : BasicTestActorInterface {
    override fun echo(msg: String): CompletableDeferred<String> {
        return CompletableDeferred(msg)
    }

    override fun waitFor(delayMs: TimeMs): Deferred<Unit> {
        return GlobalScope.async {
            delay(delayMs)
        }
    }
}

class BasicActorTest : StageBaseTest() {
    @Test
    fun `ensure basic echo has expected result`() {
        val echoMsg = "Hello Orbit!"
        val echo = stage.actorProxyFactory.getReference<BasicTestActorInterface>()
        val result = runBlocking {
            echo.echo(echoMsg).await()
        }
        assertThat(result).isEqualTo(echoMsg)
    }

    @Test
    fun `ensure basic delay causes timeout`() {
        val actor = stage.actorProxyFactory.getReference<BasicTestActorInterface>()
        assertThatThrownBy {
            runBlocking {
                actor.waitFor(stageConfig.messageTimeoutMillis + 100).await()
            }
        }.isInstanceOf(ResponseTimeoutException::class.java)
    }
}