package com.bramestorm.bassanglertracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.*
import android.util.Log

class PopupWeightEntryLbs : Activity() {
    private var selectedSpecies: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_weight_entry_lbs)

        val edtWeightLbs: EditText = findViewById(R.id.edtWeightLbs)
        val edtWeightOzs: EditText = findViewById(R.id.edtWeightOzs)
        val btnSaveWeight: Button = findViewById(R.id.btnSaveWeight)
        val btnCancel: Button = findViewById(R.id.btnCancel)
        val spinnerSpecies: Spinner = findViewById(R.id.spinnerSpeciesPopUp)

        // Get tournament species from intent (default to Large Mouth if empty)
        selectedSpecies = intent.getStringExtra("selectedSpecies") ?: "Large Mouth"

        // Load species list from strings.xml
        val speciesArray = resources.getStringArray(R.array.species_list)

        // Set up the spinner with species list
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, speciesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSpecies.adapter = adapter

        // Set the spinner selection to match the selected species
        val speciesIndex = speciesArray.indexOf(selectedSpecies)
        if (speciesIndex != -1) {
            spinnerSpecies.setSelection(speciesIndex)
        }

        // Apply InputFilters to limit values
        edtWeightLbs.filters = arrayOf(MinMaxInputFilter(1, 99)) // Lbs: 1-99
        edtWeightOzs.filters = arrayOf(MinMaxInputFilter(0, 15)) // Ozs: 0-15

        // Save button functionality
        btnSaveWeight.setOnClickListener {
            val resultIntent = Intent()
            val selectedSpeciesValue = spinnerSpecies.selectedItem.toString()
            resultIntent.putExtra("selectedSpecies", selectedSpeciesValue)

            val weightLbs = edtWeightLbs.text.toString().toIntOrNull() ?: 0
            val weightOz = edtWeightOzs.text.toString().toIntOrNull() ?: 0
            val totalWeightOz = (weightLbs * 16) + weightOz
            resultIntent.putExtra("weightTotalOz", totalWeightOz)

            Log.d("PopupWeightEntryLbs", "Selected Species: $selectedSpeciesValue")
            Log.d("PopupWeightEntryLbs", "Weight Entered: $weightLbs lbs, $weightOz oz ($totalWeightOz oz)")

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Cancel button functionality
        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    // MinMaxInputFilter to enforce value limits
    class MinMaxInputFilter(private val min: Int, private val max: Int) : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (input in min..max) {
                    return null // Accept input
                }
            } catch (e: NumberFormatException) {
                // Ignore invalid input
            }
            return "" // Reject input if out of range
        }
    }
}
