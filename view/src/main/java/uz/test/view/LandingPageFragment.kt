package uz.test.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_landing_page.view.*
import uz.test.api.IbroAplication
import uz.test.api.dto.PostAccessToken
import uz.test.api.network.Resource


class LandingPageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postDataBody = PostAccessToken(
            "2c44d8c2-c89a-472e-aab3-9a8a29142315",
            "",
            "device",
            "7db72635-fd0a-46b9-813b-1627e3aa02ea",
            0,
            0,
            0
        )

        val viewModelLandingPage = LandingPageViewModel(IbroAplication.createRepo(requireContext()))
        viewModelLandingPage.postAccessToken(postDataBody)
        viewModelLandingPage.postAccessToken.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        viewModelLandingPage.getGeneralInfo(resource.data.accessToken)
                    }
                    is Resource.GenericError -> {
                        showSnackbar(resource.errorResponse.toString())
                    }
                    is Resource.Error -> {
                        resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })

        viewModelLandingPage.getGeneralData.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val calories = String.format("%.2f", resource.data.data.currentQuantity)
                        view.bonusTextView.text = requireContext().getString(
                            R.string.receivedBonuses,
                            resource.data.data.forBurningQuantity.toInt().toString()
                        )
                        view.caloriesTextView.text =
                            requireContext().getString(R.string.calories, calories)
                        if (resource.data.data.typeBonusName.isEmpty()) {
                            view.overallBonus.text =
                                requireContext().getString(R.string.overallBonus, "0")
                        } else {
                            view.overallBonus.text = requireContext().getString(
                                R.string.overallBonus,
                                resource.data.data.typeBonusName
                            )
                        }
                    }
                    is Resource.GenericError -> {
                        showSnackbar(resource.errorResponse.toString())
                    }
                    is Resource.Error -> {
                        resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }

    fun Fragment.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
        val snackBar = Snackbar.make(requireView(), snackbarText, timeLength)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.black))
            .setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            .show()
    }


}