package example.com.totalnba.ui.player

import example.com.totalnba.R
import example.com.totalnba.arch.BaseAdapter
import example.com.totalnba.data.model.PlayerStat
import example.com.totalnba.databinding.ListItemPlayerStatBinding

class PlayerStatAdapter (
    private val list: List<PlayerStat>
) : BaseAdapter<ListItemPlayerStatBinding, PlayerStat>(list) {

    override val layoutId: Int = R.layout.list_item_player_stat

    override fun bind(binding: ListItemPlayerStatBinding, item: PlayerStat) {
        binding.apply {
            playerStat = item
            executePendingBindings()
        }
    }
}