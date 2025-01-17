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
        public class CategoryRepository : ICategoryRepository
        {
            private readonly ProductDbContext _context;

            public CategoryRepository(ProductDbContext context)
            {
                _context = context;
            }

            public async Task<IEnumerable<Category>> GetCategories()
            {
                return await _context.Categories.ToListAsync();
            }

            public async Task<Category> GetCategoryById(int categoryId)
            {
                return await _context.Categories.FirstOrDefaultAsync(c => c.Id == categoryId);
            }

            public async Task<Category> AddCategory(Category category)
            {
                _context.Categories.Add(category);
                await _context.SaveChangesAsync();

                return category;
            }

            public async Task<Category> UpdateCategory(Category category)
            {
                var existingCategory = await _context.Categories.FirstOrDefaultAsync(c => c.Id == category.Id);
                if (existingCategory == null)
                {
                    return null; // Handle case when category is not found
                }

                existingCategory.Name = category.Name;
                await _context.SaveChangesAsync();

                return existingCategory;
            }

            public async Task<Category> DeleteCategory(int categoryId)
            {
                var category = await _context.Categories.FindAsync(categoryId);
                if (category == null)
                {
                    return null;
                }

                _context.Categories.Remove(category);
                await _context.SaveChangesAsync();

                return category;
            }
        }
    }

}
