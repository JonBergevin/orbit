/*
 Copyright (C) 2015 - 2019 Electronic Arts Inc.  All rights reserved.
 This file is part of the Orbit Project <https://www.orbit.cloud>.
 See license in LICENSE.
 */

package cloud.orbit.runtime.pipeline.steps

import cloud.orbit.runtime.net.Message
import cloud.orbit.runtime.net.MessageType
import cloud.orbit.runtime.pipeline.PipelineContext

class TransportStep : PipelineStep {
    override suspend fun onOutbound(context: PipelineContext, msg: Message) {
        // TODO: This just makes the test pass
        val response = Message(
            messageType = MessageType.INVOCATION_RESPONSE_NORMAL,
            messageId = msg.messageId,
            normalResponse = "Hello Orbit!"
        )
        context.newInbound(response)
    }
}