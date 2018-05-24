package em.i.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import em.i.ImeApplication
import em.i.R
import em.i.repository.Entry
import em.i.repository.ImeDatabase
import em.i.viewmodels.StatisticsViewModel
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.util.*
import javax.inject.Inject

class StatisticsFragment : Fragment() {

    @Inject
    lateinit var database: ImeDatabase

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: StatisticsViewModel
    private var hour: Int = 0
    private var minute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { activity ->
            (activity.application as ImeApplication).appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)[StatisticsViewModel::class.java]
        with(viewModel) {
            loadEntries()
            entries?.observe(this@StatisticsFragment, Observer { entries ->

                debug.text = "${entries?.size ?: -1}"

            })
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

        button_kitchen?.setOnClickListener {
            val entry = Entry(hour = this.hour, minute = this.minute, type = 0)
            viewModel.addEntry(entry)
        }
    }

    private fun registerHourClicks() {
        button_hour_plus.setOnClickListener { handleHourClick(true) }
        button_hour_minus.setOnClickListener { handleHourClick(false) }
    }

    private fun registerMinuteClicks() {
        button_minute_plus.setOnClickListener { handleMinuteClick(true) }
        button_minute_minus.setOnClickListener { handleMinuteClick(false) }
    }

    private fun initializeCurrentTime() {
        with(Calendar.getInstance()) {
            hour = get(Calendar.HOUR_OF_DAY)
            minute = get(Calendar.MINUTE)
        }
    }

    private fun refreshTimelabel() {
        time_label.text = "%02d  :  %02d".format(hour, minute)
    }

    fun handleHourClick(isPlus: Boolean) {
        when (isPlus) {
            true -> hour++
            else -> hour--
        }
        if (hour > 23) hour = 0
        if (hour < 0) hour = 23
        refreshTimelabel()
    }

    fun handleMinuteClick(isPlus: Boolean) {
        when (isPlus) {
            true -> minute++
            else -> minute--
        }
        if (minute > 59) minute = 0
        if (minute < 0) minute = 59
        refreshTimelabel()
    }
}