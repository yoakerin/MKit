package com.yoake.umeng_share

import com.yoake.umeng_sdk.share.ShareItem

interface OnItemClickListener {
    fun onItemClick(position: Int, item: ShareItem)
}