namespace Produit_service.Repositories
{
    using global::Product_service;
    using Microsoft.EntityFrameworkCore;
    using Models;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;

    namespace Product_service.Repositories
    {
        public class ProductRepository : IProductRepository
        {
            private readonly ProductDbContext _context;

            public ProductRepository(ProductDbContext context)
            {
                _context = context;
            }

            public async Task<IEnumerable<Product>> GetProducts()
            {
                return await _context.Products.Include(p => p.Category).ToListAsync();
            }

            public async Task<Product> GetProductById(int productId)
            {
                return await _context.Products.Include(p => p.Category)
                                              .FirstOrDefaultAsync(p => p.Id == productId);
            }

            public async Task<Product> AddProduct(Product product)
            {
                var category = await _context.Categories.FindAsync(product.CategoryId);
                if (category == null)
                {
                    return null; // Or handle this case appropriately (e.g., throw an exception)
                }

                product.Category = category;
                _context.Products.Add(product);
                await _context.SaveChangesAsync();

                return product;
            }

            public async Task<Product> UpdateProduct(Product product)
            {
                var existingProduct = await _context.Products.FirstOrDefaultAsync(p => p.Id == product.Id);
                if (existingProduct == null)
                {
                    return null; // Handle case when product is not found
                }

                existingProduct.Name = product.Name;
                existingProduct.Price = product.Price;
                existingProduct.StockQuantity = product.StockQuantity;
                existingProduct.CategoryId = product.CategoryId;
                existingProduct.Category = await _context.Categories.FindAsync(product.CategoryId);

                await _context.SaveChangesAsync();

                return existingProduct;
            }
            public async Task<IEnumerable<Product>> GetOutOfStockProducts()
            {
                return await _context.Products
                                     .Where(p => p.StockQuantity <= 0)
                                     .ToListAsync();
            }
            public async Task<Product> UpdateStock(int productId, int quantityChange)
            {
                var product = await _context.Products.FirstOrDefaultAsync(p => p.Id == productId);
                if (product == null) return null;

                product.StockQuantity += quantityChange; // quantityChange peut être négatif
                await _context.SaveChangesAsync();

                return product;
            }
            public async Task<Product> DeleteProduct(int productId)
            {
                var product = await _context.Products.FindAsync(productId);
                if (product == null)
                {
                    return null;
                }

                _context.Products.Remove(product);
                await _context.SaveChangesAsync();

                return product;
            }
        }
    }

}
