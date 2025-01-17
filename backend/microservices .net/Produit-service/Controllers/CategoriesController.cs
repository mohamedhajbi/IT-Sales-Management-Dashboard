using Microsoft.AspNetCore.Mvc;
using Models;
using Produit_service.Repositories;

namespace Product_service.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CategoriesController : ControllerBase
    {
        private readonly ICategoryRepository _categoryRepository;

        // Injecting the ICategoryRepository into the controller
        public CategoriesController(ICategoryRepository categoryRepository)
        {
            _categoryRepository = categoryRepository; 
        }

        // GET: api/categories
        [HttpGet]
        public async Task<IActionResult> GetAllCategories()
        {
            var categories = await _categoryRepository.GetCategories();
            return Ok(categories);
        }

        // GET: api/categories/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetCategoryById(int id)
        {
            var category = await _categoryRepository.GetCategoryById(id);
            if (category == null)
            {
                return NotFound();
            }
            return Ok(category);
        }

        // POST: api/categories
        [HttpPost]
        public async Task<IActionResult> AddCategory(Category category)
        {
            if (category == null)
            {
                return BadRequest("Invalid category data.");
            }

            var createdCategory = await _categoryRepository.AddCategory(category);
            return CreatedAtAction(nameof(GetCategoryById), new { id = createdCategory.Id }, createdCategory);
        }

        // PUT: api/categories/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateCategory(int id, Category category)
        {
            if (category == null || category.Id != id)
            {
                return BadRequest();
            }

            var updatedCategory = await _categoryRepository.UpdateCategory(category);
            if (updatedCategory == null)
            {
                return NotFound();
            }

            return Ok(updatedCategory);
        }

        // DELETE: api/categories/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCategory(int id)
        {
            var deletedCategory = await _categoryRepository.DeleteCategory(id);
            if (deletedCategory == null)
            {
                return NotFound();
            }

            return NoContent(); // No content is returned on successful delete
        }
    }
}
