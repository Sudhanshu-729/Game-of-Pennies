package hey.there.pennydrop.binding

class Solution {
    fun numberOfPairs(nums: IntArray): IntArray {
        var count=0
        var map:MutableMap<Int,Int> = mutableMapOf()
        for(num in nums) {
            if (!map.containsKey(num))
                map.put(num,1)
            else{
                map[num]= map[num]!!.plus(1)
            }
        }
        for(value in map.values){
            if(value>1){
                count+=(value/2)
            }
        }
        return intArrayOf(count,nums.size-count*2)


    }
}