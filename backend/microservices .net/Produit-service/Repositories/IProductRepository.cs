using Models;

namespace Produit_service.Repositories
{
    public interface IProductRepository
    {
        Task<IEnumerable<Product>> GetProducts();
        Task<Product> GetProductById(int productId);
        Task<Product> AddProduct(Product product);
        Task<Product> UpdateProduct(Product product);
        Task<Product> DeleteProduct(int productId);
        Task<IEnumerable<Product>> GetOutOfStockProducts();
        Task<Product> UpdateStock(int productId, int quantityChange);

    }
}
