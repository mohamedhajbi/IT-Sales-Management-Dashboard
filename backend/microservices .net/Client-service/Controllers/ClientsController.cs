using Microsoft.AspNetCore.Mvc;
using Client_service.Repositories;
using Models;

namespace Client_service.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class ClientsController : ControllerBase
    {
        private readonly IClientRepository _clientRepository;

        public ClientsController(IClientRepository clientRepository)
        {
            _clientRepository = clientRepository;
        }

        // GET: api/clients
        [HttpGet]
        public async Task<IActionResult> GetAllClients()
        {
            var clients = await _clientRepository.GetClients();
            return Ok(clients);
        }

        // GET: api/clients/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetClientById(int id)
        {
            var client = await _clientRepository.GetClient(id);
            if (client == null)
            {
                return NotFound();
            }
            return Ok(client);
        }

        // POST: api/clients
        [HttpPost]
        public async Task<IActionResult> AddClient(Client client)
        {
            if (client == null)
            {
                return BadRequest("Invalid client data.");
            }

            var createdClient = await _clientRepository.AddClient(client);
            return CreatedAtAction(nameof(GetClientById), new { id = createdClient.Id }, createdClient);
        }

        // PUT: api/clients/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateClient(int id, Client client)
        {
            if (client == null || client.Id != id)
            {
                return BadRequest();
            }

            var updatedClient = await _clientRepository.UpdateClient(client);
            if (updatedClient == null)
            {
                return NotFound();
            }

            return Ok(updatedClient);
        }

        // DELETE: api/clients/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteClient(int id)
        {
            var deletedClient = await _clientRepository.DeleteClient(id);
            if (deletedClient == null)
            {
                return NotFound();
            }

            return NoContent();
        }
    }
}
