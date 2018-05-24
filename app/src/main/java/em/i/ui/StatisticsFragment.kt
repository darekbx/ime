package em.i.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import em.i.ImeApplication
import em.i.R
import em.i.repository.ImeDatabase
import java.sql.Date
import java.util.*
import javax.inject.Inject

class StatisticsFragment : Fragment() {

    @Inject
    lateinit var database: ImeDatabase

    private var hour: Int  = 0
    private var minute: Int  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as ImeApplication).appComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeCurrentTime()
        refreshTimelabel()

        registerHourClicks()
        registerMinuteClicks()
    }

    private fun registerHourClicks() {
        hourPlus.setOnClickListener { handleHourClick(true) }
        hourMinus.setOnClickListener { handleHourClick(false) }
    }

    private fun registerMinuteClicks() {
        minutePlus.setOnClickListener { handleMinuteClick(true) }
        minuteMinus.setOnClickListener { handleMinuteClick(false) }
    }

    private fun initializeCurrentTime() {
        with(Calendar.getInstance()) {
            hour = get(Calendar.HOUR_OF_DAY)
            minute = get(Calendar.MINUTE)
        }
    }

    private fun refreshTimelabel() {
        timeLabel.text = "%02d  :  %02d".format(hour,  minute)
    }

    fun handleHourClick(isPlus: Boolean) {
        when(isPlus) {
            true -> hour++
            else -> hour--
        }
        if (hour > 23) hour = 0
        if (hour < 0) hour = 23
        refreshTimelabel()
    }

    fun handleMinuteClick(isPlus: Boolean) {
        when(isPlus) {
            true -> minute++
            else -> minute--
        }
        if (minute > 59) minute = 0
        if (minute < 0) minute = 59
        refreshTimelabel()
    }

    val hourPlus by lazy { view?.findViewById(R.id.button_hour_plus) as View }
    val hourMinus by lazy { view?.findViewById(R.id.button_hour_minus) as View }
    val minutePlus by lazy { view?.findViewById(R.id.button_minute_plus) as View }
    val minuteMinus by lazy { view?.findViewById(R.id.button_minute_minus) as View }
    val timeLabel by lazy { view?.findViewById(R.id.time_label) as TextView }
}
