import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eseul.yobunjeong.databinding.ItemStoreBinding

class StoreAdapter(
    private val items: List<StoreItem>,
    private val onClick: (StoreItem) -> Unit
) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoreItem) {
            binding.storeImage.setImageResource(item.imageRes) // 이미지 설정
            binding.storeTitle.text = item.title              // 제목 설정
            binding.storePrice.text = item.price              // 가격 설정
            binding.buyButton.setOnClickListener { onClick(item) } // 클릭 이벤트
        }
    }
}

data class StoreItem(
    val imageRes: Int,  // 이미지 리소스 ID
    val title: String,  // 상품 제목
    val price: String   // 상품 가격
)
