import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.widget.ImageView
import com.example.myapplication.R
import java.net.URL

@Suppress("DEPRECATION")
class DominantColorTask(private val context: Context, firstOval : ImageView, secondOval : ImageView) : AsyncTask<String, Void, Pair<String, String>>() {
    override fun doInBackground(vararg params: String?): Pair<String, String> {
        val imageUrl = params[0]
        val bitmap = BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())


        // Use a map to store the frequency of each color
        val colorFrequencyMap = mutableMapOf<Int, Int>()

        // Loop through each pixel of the image
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val color = bitmap.getPixel(x, y)
                colorFrequencyMap[color] = (colorFrequencyMap[color] ?: 0) + 1
            }
        }

        // Sort the colors by frequency
        val sortedColors = colorFrequencyMap.toList().sortedByDescending { (_, value) -> value }

        // Extract the two most frequent colors
        val dominantColor1 = sortedColors[0].first
        val dominantColor2 = sortedColors[1].first

        // Convert the colors to hexadecimal representation
        val hexColor1 = String.format("#%06X", 0xFFFFFF and dominantColor1)
        val hexColor2 = String.format("#%06X", 0xFFFFFF and dominantColor2)

        return Pair(hexColor1, hexColor2)
    }

    override fun onPostExecute(result: Pair<String, String>) {
       /* super.onPostExecute(result)
        val hexColor1 = result.first
        val hexColor2 = result.second

        val color1 = Color.parseColor(hexColor1)
        val color2 = Color.parseColor(hexColor2)

        val firstOval = this.resources.getDrawable(R.drawable.circle_green) as GradientDrawable
        secondOval.setBackgroundColor(color2)*/
        super.onPostExecute(result)

    }
}