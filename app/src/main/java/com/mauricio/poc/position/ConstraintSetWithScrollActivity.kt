package com.mauricio.poc.position

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.mauricio.poc.position.adapter.ActionWidthResolver
import com.mauricio.poc.position.adapter.ActionsAdapter
import com.mauricio.poc.position.adapter.SpaceItemDecoration
import com.mauricio.poc.position.data.MenuActionProvider
import com.mauricio.poc.position.data.PatrimonyLoading
import com.mauricio.poc.position.data.ProfitabilityLoading
import com.mauricio.poc.position.data.Repository
import com.mauricio.poc.position.viewmodel.HomeWithInvestmentsViewModel
import kotlinx.android.synthetic.main.activity_constraintset_with_scroll.*

class ConstraintSetWithScrollActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeWithInvestmentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraintset_with_scroll)

        viewModel = HomeWithInvestmentsViewModel(MenuActionProvider(this))

        setupPatrimonyView()
        setupProfitabilityView()
        setupViews()
    }

    private fun setupViews() {
        viewModel.actions.observe(this, Observer { menus ->
            rvEngagementActions.apply {
                val widthResolver = ActionWidthResolver(
                    horizontalSpaceDimen = R.dimen.medium_margin, horizontalSpaceCount = 3, numberOfElementsPerRow = 2
                )
                adapter = ActionsAdapter(menus, widthResolver)
                layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
                addItemDecoration(
                    SpaceItemDecoration(
                        context,
                        SpaceItemDecoration.Space(top = R.dimen.medium_margin, left = R.dimen.medium_margin)
                    )
                )
            }
        })
    }

    private fun setupPatrimonyView() = with(patrimonyView) {
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
        setupState(Repository.loadPatrimonyData())
        setTryAgainClickListener {
            patrimonyView.setupState(PatrimonyLoading)
            postDelayed({
                patrimonyView.setupState(Repository.loadPatrimonyData())
            }, 1000)
        }
    }

    private fun setupProfitabilityView() = with(profitabilityView) {
        setTransitionListener { transition ->
            TransitionManager.beginDelayedTransition(content, transition)
        }
        setupState(Repository.loadProfitabilityData())
        setTryAgainClickListener {
            profitabilityView.setupState(ProfitabilityLoading)
            postDelayed({
                profitabilityView.setupState(Repository.loadProfitabilityData())
            }, 1000)
        }
    }
}