package com.github.quiltservertools.ledger.actions

import com.github.quiltservertools.ledger.actionutils.Preview
import com.github.quiltservertools.ledger.config.ActionsSpec
import com.github.quiltservertools.ledger.config.config
import com.mojang.authlib.GameProfile
import java.time.Instant
import net.minecraft.block.BlockState
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import kotlin.time.ExperimentalTime

interface ActionType {
    val identifier: String
    var timestamp: Instant
    var pos: BlockPos
    var world: Identifier?
    var objectIdentifier: Identifier
    var oldObjectIdentifier: Identifier
    var blockState: BlockState?
    var oldBlockState: BlockState?
    var sourceName: String
    var sourceProfile: GameProfile?
    var extraData: String?
    var rolledBack: Boolean

    fun rollback(server: MinecraftServer): Boolean
    fun restore(server: MinecraftServer): Boolean
    fun previewRollback(preview: Preview, player: ServerPlayerEntity)
    fun previewRestore(preview: Preview, player: ServerPlayerEntity)
    fun getTranslationType(): String
    @ExperimentalTime
    fun getMessage(): Text

    fun isBlacklisted() = config[ActionsSpec.typeBlacklist].contains(identifier) ||
            config[ActionsSpec.objectBlacklist].contains(objectIdentifier) ||
            config[ActionsSpec.objectBlacklist].contains(oldObjectIdentifier) ||
            config[ActionsSpec.sourceBlacklist].contains(sourceName) ||
            config[ActionsSpec.worldBlacklist].contains(world)
}
