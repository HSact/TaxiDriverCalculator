package com.example.taxidrivercalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.taxidrivercalculator.databinding.FragmentAddShiftBinding
import com.example.taxidrivercalculator.ui.DatePickerFragment
import com.example.taxidrivercalculator.ui.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddShift : Fragment(R.layout.fragment_add_shift) {

    object CalcShift {
        var date: String =""
        var onlineTime: Long = 0
        var breakTime: Long = 0
        var totalTime: Long = 0
        var earnings: Double = 0.0
        var wash: Double = 0.0
        var fuelCost: Double = 0.0
        var mileage: Double = 0.0
        var profit: Double = 0.0
    }

    lateinit var currentShift: CalcShift


    lateinit var editDate: EditText
    lateinit var editStart: EditText
    lateinit var editEnd: EditText
    lateinit var checkBreak: CheckBox
    lateinit var editBreakStart: EditText
    lateinit var editBreakEnd: EditText
    lateinit var editEarnings: EditText
    lateinit var editWash: EditText
    lateinit var editFuelCost: EditText
    lateinit var editMileage: EditText
    lateinit var buttonSubmit: Button


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddShiftBinding? = null
    private val binding get() = _binding!!

    /*private var _binding2: ActivityMainBinding? = null
    private val binding2 get() = _binding2!!*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainActivity.botNav.isVisible = false

        /*supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "About"*/


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
        //navView.visibility = View.GONE


    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddShiftBinding.inflate(inflater, container, false)
        currentShift=CalcShift

        bindItems()
        loadGuess()

        //pickDate()

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_shift, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*editDate.setOnClickListener() {
            pickDate()
        }*/
        editDate.setOnFocusChangeListener { view, b ->  if (editDate.isFocused) pickDate(editDate)}
        editStart.setOnFocusChangeListener { view, b ->  if (editStart.isFocused) pickTime(editStart)}
        editEnd.setOnFocusChangeListener { view, b ->  if (editEnd.isFocused) pickTime(editEnd)}
        editBreakStart.setOnFocusChangeListener { view, b ->  if (editBreakStart.isFocused) pickTime(editBreakStart)}
        editBreakEnd.setOnFocusChangeListener { view, b ->  if (editBreakEnd.isFocused) pickTime(editBreakEnd)}
        checkBreak.setOnClickListener { switchBrake() }
        buttonSubmit.setOnClickListener {calculateShift()}

    }

    private fun bindItems ()
    {
        editDate = binding.buttonDatePick
        editStart = binding.buttonTimeStart
        editEnd = binding.buttonTimeEnd
        checkBreak = binding.checkBreak
        editBreakStart = binding.buttonTimeStartBrake
        editBreakEnd = binding.buttonTimeEndBrake
        editEarnings = binding.editTextEarnings
        editWash = binding.editTextWash
        editFuelCost = binding.editTextFuelCost
        editMileage = binding.editTextMileage
        buttonSubmit = binding.buttonSubmit
    }

    @SuppressLint("SimpleDateFormat")
    private fun loadGuess ()
    {
        val dateFormat = SimpleDateFormat ("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat ("H:mm")
        val currentDate = dateFormat.format(Date())
        val currentTime = timeFormat.format(Date())
        editDate.setText(currentDate)
        editEnd.setText(currentTime)
        val a = convertLongToTime(convertTimeToLong(currentTime)-hoursToMs(10))
        editStart.setText(a)
        //System.currentTimeMillis()

    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("H:mm")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimeToLong(date: String): Long {
        val df = SimpleDateFormat("H:mm")
        return df.parse(date).time
    }
    private fun hoursToMs (hours: Int): Long
    {
        return (hours*60*60*1000).toLong()
    }
    private fun msToHours (ms: Long) : Double
    {
        return ((ms/60.0/60.0/1000.0)*100.0).toInt()/100.0
    }
    /*fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }*/

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pickDate(editObj: EditText)
    {
        // create new instance of DatePickerFragment
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager

        // we have to implement setFragmentResultListener
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
                viewLifecycleOwner)
        { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                editObj.setText(date.toString())
            }
        }

        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }

    private fun pickTime(editObj: EditText)
    {

        // create new instance of DatePickerFragment
        val timePickerFragment = TimePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager


        // we have to implement setFragmentResultListener
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner)
        { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val time = bundle.getString("SELECTED_TIME")
                editObj.setText(time.toString())
            }
        }

        // show
        timePickerFragment.show(supportFragmentManager, "TimePickerFragment")

    }

    private fun calculateShift ()
    {
        currentShift.onlineTime = convertTimeToLong(editEnd.text.toString()) - convertTimeToLong(editStart.text.toString())
        if (currentShift.onlineTime<0)
        {
            currentShift.onlineTime += hoursToMs(24)
        }
        if (checkBreak.isChecked)
        {
            if (editBreakStart.text.toString()=="")
            {
                showErrorMessage("empty_BreakStart")
                return
            }
            if (editBreakEnd.text.toString()=="")
            {
                showErrorMessage("empty_BreakEnd")
                return
            }
            currentShift.breakTime=convertTimeToLong(editBreakEnd.text.toString())-convertTimeToLong(editBreakStart.text.toString())
            if (currentShift.breakTime<0)
            {
                currentShift.breakTime += hoursToMs(24)
            }
            currentShift.totalTime=currentShift.onlineTime-currentShift.breakTime

        }
        else
        {
            currentShift.totalTime=currentShift.onlineTime
        }



        /*if (currentShift.totalTime > hoursToMs(12))
        {
            showWarningMessage("Your shift was for ${msToHours(currentShift.totalTime)} hours?")
        }
        if (currentShift.totalTime < hoursToMs(4))
        {
            showWarningMessage("Your shift was for ${msToHours(currentShift.totalTime)} hours?")
        }
        if (currentShift.breakTime > hoursToMs(4))
        {
            showWarningMessage("Your brake was for ${msToHours(currentShift.breakTime)} hours?")
        }*/
        if (editEarnings.text.isEmpty())
        {
            showErrorMessage("empty_Earnings")
            return
        }
        if (editWash.text.isEmpty())
        {
            editWash.setText("0")
        }
        if (editFuelCost.text.isEmpty())
        {
            showErrorMessage("empty_Fuel")
            return
        }
        if (editMileage.text.isEmpty())
        {
            showErrorMessage("empty_Mileage")
            return
        }

        currentShift.earnings=editEarnings.text.toString().toDouble()
        currentShift.wash=editWash.text.toString().toDouble()
        currentShift.fuelCost=editFuelCost.text.toString().toDouble()
        currentShift.mileage=editMileage.text.toString().toDouble()


        currentShift.profit= currentShift.earnings-currentShift.wash-currentShift.fuelCost

        showSubmitMessage("Sure? You earn ${currentShift.profit} in ${msToHours(currentShift.totalTime)} hours?")

    }

    private fun showErrorMessage(errorCode: String)
    {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Error")
        alert.setPositiveButton("OK", null)
        alert.setMessage(errorCode)
        alert.show()

    }
    /*private fun showWarningMessage (warningCode: String)
    {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Warning")
        alert.setPositiveButton("OK", null)
        alert.setNegativeButton("CANCEL") {dialog, id -> onResume()}
        alert.setMessage(warningCode)
        alert.show()

    }*/
    private fun showSubmitMessage (warningCode: String)
    {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Submit")
        alert.setPositiveButton("OK") {dialog, id -> submit()}
        alert.setNegativeButton("CANCEL", null)
        alert.setMessage(warningCode)
        alert.show()
    }

    private fun submit()
    {
        currentShift.date=editDate.text.toString()

        /*val toast = Toast(activity)
        toast.setText("Success!")*/
        val db = DBHelper(requireActivity(), null)
        db.addShift(currentShift.date, msToHours(currentShift.totalTime),
            currentShift.earnings, currentShift.wash, currentShift.fuelCost, currentShift.mileage, currentShift.profit)

        Toast.makeText(activity,"Suck ass!",Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addShift_to_home_fragment)
        MainActivity.botNav.isVisible = true
    }




    override fun onResume() {
        MainActivity.botNav.isVisible = false
        super.onResume()
    }

    override fun onDestroy() {
        MainActivity.botNav.isVisible = true
        super.onDestroy()
    }

    private fun switchBrake()
    {
        val check1: CheckBox = binding.checkBreak
        val row = binding.tableBreak
        row.isVisible = check1.isChecked
    }




    companion object {

    }

    /*fun setTime()
    {
        var b: Int
        var c: Int
        var a = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = {view, b, c},-> b=b)

    }*/
    /*@RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        /*fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()*/
        val tpd = TimePickerDialog(context,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            Toast.makeText(context, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()

        }),hour,minute,false)

        tpd.show()
    }*/


}