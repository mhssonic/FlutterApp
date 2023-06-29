package com.mhssonic.flutter.ui.userAuth.sign_up

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.mhssonic.flutter.R
import java.util.Calendar
import java.util.Locale


class SignUpFourth : SignUp() {
    private lateinit var btnRegisterFragment: Button
    private lateinit var btnPreFragment: Button

    private lateinit var spinnerCountry: Spinner
    private lateinit var edBirthDate: EditText
    private var selectedDay: Int = 0
    private var selectedMonth: Int = 0
    private var selectedYear: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up_fourth, container, false)

        btnRegisterFragment = view.findViewById(R.id.btRegister)
        btnPreFragment = view.findViewById(R.id.brPrev)

        spinnerCountry = view.findViewById(R.id.spinnerCountry)

        val countryList = getCountryList()
        countryList.add(0,"کشور")


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCountry.adapter = adapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCountry = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                emptyToast()
            }
        }



        edBirthDate = view.findViewById(R.id.editTextBirthDate)
        edBirthDate.setOnClickListener {
            showDate()
        }


        var birthDate = "$selectedDay-$selectedMonth-$selectedYear\""

        btnRegisterFragment.setOnClickListener {

            val fourthFragment = SignUpFourth()

            if (1 == 0) {
                emptyToast()
            } else {
                val bundle = Bundle()
                fourthFragment.arguments = bundle
                Log.i("MYTAG", "$birthDate")


                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fourthFragment)
                    .addToBackStack(null)
                    .commit()
            }

        }

        btnPreFragment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun showDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                this.selectedDay = selectedDayOfMonth
                this.selectedMonth = selectedMonth + 1
                this.selectedYear = selectedYear
                val formattedDate = "${selectedDayOfMonth}/${selectedMonth + 1}/${selectedYear}"
                edBirthDate.setText(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun getCountryList(): MutableList<String> {
        val localeList = Locale.getAvailableLocales()
        val countryList = mutableListOf<String>()
        for (locale in localeList) {
            val country = locale.displayCountry
            if (country.isNotEmpty() && !countryList.contains(country)) {
                countryList.add(country)
            }
        }

        return countryList.sorted().toMutableList()
    }
}