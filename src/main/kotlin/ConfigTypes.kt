import ConfigTypes.authConfigData
import ConfigTypes.mutesConfigData
import ConfigTypes.permissionConfigData
import ConfigTypes.rulesConfigData
import ConfigTypes.userConfigData

object ConfigTypes {
    var authConfigData: AuthConfig? = null
    var mutesConfigData: MuteConfig? = null
    var userConfigData: UserConfig? = null
    var permissionConfigData: PermissionConfig? = null
    var rulesConfigData: RulesConfig? = null
}

/**
 * [configPath] is the file name on disk, OR a remote URL. If it is a URL, it must be a valid URL which includes http/https as a prefix
 * [data] is the actual config data, read in the format of clazz
 */
enum class ConfigType(val configPath: String, var data: Any?) {
    AUTH("config/auth.json", authConfigData),
    RULES("config/rules.json", rulesConfigData),
    MUTE("config/mutes.json", mutesConfigData),
    USER("config/user.json", userConfigData),
    PERMISSION("config/permissions.json", permissionConfigData)
}

/**
 * [botToken] is the token given to you from https://discord.com/developers/applications/BOT_ID_HERE/bot
 * [githubToken] can be generated with the full "repo" access checked https://github.com/settings/tokens
 */
data class AuthConfig(val botToken: String, val githubToken: String)

/**
 * [id] is the user snowflake ID
 * [unixUnmute] is the UNIX time of when they should be unmuted.
 * When adding a new [unixUnmute] time, it should be current UNIX time + mute time in seconds
 */
data class MuteConfig(val id: Long, val unixUnmute: Long)

/**
 * [rules] is a HashMap with the rule name/number as the key and the rule as the value
 */
data class RulesConfig(val rules: HashMap<String, String>)

/**
 * [version] is a semver format version String
 * Checked by comparing [Main.currentVersion] against https://raw.githubusercontent.com/kami-blue/bot-kt/master/version.json
 */
data class VersionConfig(val version: String)

/**
 * [autoUpdate] is whether the bot should automatically update after a successful update check. Will not do anything when set to true if update checking is disabled.
 * [primaryServerId] is the main server where startup messages should be sent. Omit from config to send to all servers.
 * [startUpChannel] is the channel name of where to send bot startup messages. Omit from config to disable startup messages.
 * [statusMessage] is the bot status message on Discord
 * [statusMessageType] is the type of status. Playing is 0, Streaming is 1, Listening is 2 and Watching is 3.
 */
data class UserConfig(val autoUpdate: Boolean?, val autoUpdateRestart: Boolean?, val primaryServerId: Long?, val startUpChannel: String?, val statusMessage: String?, val statusMessageType: Int?)

/**
 * [councilMembers] is a hashmap of all the council members
 */
data class PermissionConfig(val councilMembers: HashMap<Long, List<PermissionTypes>>)
