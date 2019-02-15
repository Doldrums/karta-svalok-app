package com.teamtwothree.kartasvalokapp.presentation.results.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.BuildConfig
import com.teamtwothree.kartasvalokapp.R
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.service.dateToReadable
import kotlinx.android.synthetic.main.item_report.view.*
import kotlinx.android.synthetic.main.item_report_expanded.view.*

class PointDetailsHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var expanded = false

    fun bind(pointDetails: PointDetails) {
        view.tv_report_address.text = pointDetails.address
        view.tv_report_date.text = dateToReadable(pointDetails.createdAt.toLong())
        view.chip_report_status.text = pointDetails.status
        view.tv_report_subject.text = pointDetails.subject
        view.tv_report_description.text = pointDetails.description
        view.tv_report_comment.text = pointDetails.comment

        spawnPhotosInParent(pointDetails.photo, view.report_layout_photo)
        spawnPhotosInParent(pointDetails.photoAdmin, view.report_layout_photo_admin)

        view.setOnClickListener {
            //onClickListener: ReportsAdapter.OnItemClickListener
            //onClickListener.onClick(this.adapterPosition)
            when (expanded) {
                true -> {
                    view.iv_expand.setImageDrawable(AppDelegate.applicationContext().getDrawable(R.drawable.ic_expand))
                    view.report_expanded.visibility = View.GONE
                }
                false -> {
                    view.iv_expand.setImageDrawable(AppDelegate.applicationContext().getDrawable(R.drawable.ic_collapse))
                    view.report_expanded.visibility = View.VISIBLE
                }
            }
            expanded = !expanded
        }
    }

    private fun dipsToPixels(sizeInDp: Float): Int =
        (sizeInDp * AppDelegate.applicationContext().resources.displayMetrics.density + 0.5f).toInt()


    private fun spawnPhotosInParent(list: List<String>, parent: ViewGroup) {
        for (photo in list) {
            val imageViewParams = ViewGroup.LayoutParams(dipsToPixels(128f), dipsToPixels(192f))
            val imageView = ImageView(AppDelegate.applicationContext()).also { it.layoutParams = imageViewParams }
            imageView.layoutParams = imageViewParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(view).load(BuildConfig.API_URL + photo).into(imageView)
            parent.addView(imageView)
        }
    }
}

