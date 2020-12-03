package com.example.week3day5.app


class EndPoints {
    companion object{

        private val CATEGORY_ENDPOINT = "category"
        private val SUB_CATEGORY_ENDPOINT = "subcategory/"
        private val PRODUCT_ENDPOINT = "products/sub/"
        private val PRODUCT_SEARCH_ENDPOINT = "products/search/"

        private val REGISTER_POST_ENDPOINT = "auth/register"
        private val LOGIN_POST_ENDPOINT = "auth/login"

        private val ADDRESS_ENDPOINT = "address/"
        private val ORDER_ENDPOINT = "orders"

        fun getImageUrl(image: String): String{
            return Config.IMAGE_URL + image
        }

        fun getRegisterPostUrl(): String{
            return Config.BASE_URL + REGISTER_POST_ENDPOINT
        }

        fun getLoginPostUrl(): String{
            return Config.BASE_URL + LOGIN_POST_ENDPOINT
        }

        fun getCategoryUrl(): String{
            return Config.BASE_URL + CATEGORY_ENDPOINT
        }

        fun getSubCategoryUrl(catId: Int): String{
            return Config.BASE_URL + SUB_CATEGORY_ENDPOINT + catId
        }

        fun getProductUrl(subId: Int): String{
            return Config.BASE_URL + PRODUCT_ENDPOINT + subId
        }

        fun getProductSearchUrl(productName: String?): String{
            return Config.BASE_URL + PRODUCT_SEARCH_ENDPOINT + productName
        }

        fun getAddressGetUrl(userId: String): String{
            return Config.BASE_URL + ADDRESS_ENDPOINT + userId
        }

        fun getAddressPostUrl(): String{
            return Config.BASE_URL + ADDRESS_ENDPOINT
        }

        fun getOrderPostUrl(): String{
            return Config.BASE_URL + ORDER_ENDPOINT
        }

        fun getOrderGetUrl(userId: String): String{
            return Config.BASE_URL + ORDER_ENDPOINT + "/" + userId
        }
    }
}