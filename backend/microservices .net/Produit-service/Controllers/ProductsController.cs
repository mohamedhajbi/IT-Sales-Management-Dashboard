using Microsoft.AspNetCore.Mvc;
using Models;
using Produit_service.Repositories;


namespace Product_service.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ProductsController : ControllerBase
    {
        private readonly IProductRepository _productRepository;
        private readonly ICategoryRepository _categoryRepository;

        public ProductsController(IProductRepository productRepository, ICategoryRepository categoryRepository)
        {
            _productRepository = productRepository;
            _categoryRepository = categoryRepository;
        }

        [HttpGet]
        public async Task<IActionResult> GetAllProducts()
        {
            var products = await _productRepository.GetProducts();
            return Ok(products);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetProductById(int id)
        {
            var product = await _productRepository.GetProductById(id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }

        [HttpPost]
        public async Task<IActionResult> AddProduct(Product product)
        {
            var addedProduct = await _productRepository.AddProduct(product);
            if (addedProduct == null)
            {
                return BadRequest("Category not found.");
            }
            return CreatedAtAction(nameof(GetProductById), new { id = addedProduct.Id }, addedProduct);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateProduct(int id, Product product)
        {
            if (id != product.Id)
            {
                return BadRequest();
            }

            var updatedProduct = await _productRepository.UpdateProduct(product);
            if (updatedProduct == null)
            {
                return NotFound();
            }

            return Ok(updatedProduct);
        }
        [HttpGet("out-of-stock")]
        public async Task<IActionResult> GetOutOfStockProducts()
        {
            var products = await _productRepository.GetOutOfStockProducts();
            return Ok(products);
        }

        [HttpPut("{id}/update-stock")]
        public async Task<IActionResult> UpdateStock(int id, [FromBody] int quantityChange)
        {
            var updatedProduct = await _productRepository.UpdateStock(id, quantityChange);
            if (updatedProduct == null)
            {
                return NotFound("Produit non trouvé");
            }
            return Ok(updatedProduct);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteProduct(int id)
        {
            var product = await _productRepository.DeleteProduct(id);
            if (product == null)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
