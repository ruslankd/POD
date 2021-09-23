package com.example.pod

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


class CosmosLayoutBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<ImageView>(context, attrs) {

    private var mContext: Context = context

    private val mCustomFinalYPosition = 0f
    private val mCustomStartXPosition = 0f
    private val mCustomStartToolbarPosition = 0f
    private val mCustomStartHeight = 0f
    private val mCustomFinalHeight = 0f

    private val mAvatarMaxSize = 0f
    private val mFinalLeftAvatarPadding = 0f
    private val mStartPosition = 0f
    private var mStartXPosition = 0
    private var mStartToolbarPosition = 0f
    private var mStartYPosition = 0
    private var mFinalYPosition = 0
    private var mStartHeight = 0
    private var mFinalXPosition = 0
    private var mChangeBehaviorPoint = 0f

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
//        maybeInitProperties(child, dependency)
//
//        val maxScrollDistance = mStartToolbarPosition.toInt()
//        val expandedPercentageFactor: Float = dependency.y / maxScrollDistance
//
//        if (true) {
//            val heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint
//            val distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
//                    * heightFactor) + child.height / 2
//            val distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
//                    * (1f - expandedPercentageFactor)) + child.height / 2
//            child.x = mStartXPosition - distanceXToSubtract
//            child.y = mStartYPosition - distanceYToSubtract
//            val heightToSubtract = (mStartHeight - mCustomFinalHeight) * heightFactor
//            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
//            lp.width = (mStartHeight - heightToSubtract).toInt()
//            lp.height = (mStartHeight - heightToSubtract).toInt()
//            child.layoutParams = lp
//        } else {
//            val distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
//                    * (1f - expandedPercentageFactor)) + mStartHeight / 2
//            child.x = (mStartXPosition - child.width / 2).toFloat()
//            child.y = mStartYPosition - distanceYToSubtract
//            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
//            lp.width = mStartHeight
//            lp.height = mStartHeight
//            child.layoutParams = lp
//        }

        val minWidth = mContext.resources.getDimension(R.dimen.image_final_width).toInt()
        val appBarBottom = (dependency as AppBarLayout).bottom
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.width =  appBarBottom
        lp.height =  appBarBottom
        child.layoutParams = lp

        if (appBarBottom < minWidth) child.visibility = View.INVISIBLE
        else child.visibility = View.VISIBLE

        return true
    }

    private fun maybeInitProperties(child: ImageView, dependency: View) {
        if (mStartYPosition == 0) mStartYPosition = dependency.y.toInt()
        if (mFinalYPosition == 0) mFinalYPosition = dependency.height / 2
        if (mStartHeight == 0) mStartHeight = child.height
        if (mStartXPosition == 0) mStartXPosition = (child.x + child.width / 2).toInt()
        if (mFinalXPosition == 0) mFinalXPosition = mContext.resources.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + mCustomFinalHeight.toInt() / 2
        if (mStartToolbarPosition == 0f) mStartToolbarPosition = dependency.y
        if (mChangeBehaviorPoint == 0f) {
            mChangeBehaviorPoint = (child.height - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition))
        }
    }

}