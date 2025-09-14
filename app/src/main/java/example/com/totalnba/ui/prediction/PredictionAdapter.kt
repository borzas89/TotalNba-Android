package example.com.totalnba.ui.prediction

import example.com.totalnba.R
import example.com.totalnba.arch.BaseAdapter
import example.com.totalnba.data.model.PredictedMatch
import example.com.totalnba.databinding.ListItemPredictionBinding

class PredictionAdapter (
    private val list: List<PredictedMatch>,
    private val predictionListener: PredictionListener
) : BaseAdapter<ListItemPredictionBinding, PredictedMatch>(list) {

    override val layoutId: Int = R.layout.list_item_prediction

    override fun bind(binding: ListItemPredictionBinding, item: PredictedMatch) {
        binding.apply {
            prediction = item
            listener = predictionListener
            executePendingBindings()
        }
    }
}

interface PredictionListener {
    fun onPredictionClicked(prediction: PredictedMatch)
    fun onTeamClicked(teamName: String, opponentName: String)
}