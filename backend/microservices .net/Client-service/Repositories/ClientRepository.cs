using Microsoft.EntityFrameworkCore;
using Models;

namespace Client_service.Repositories
{
    public class ClientRepository : IClientRepository
    {
        private readonly ClientDbContext _context;

        public ClientRepository(ClientDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Client>> GetClients()
        {
            return await _context.Clients.ToListAsync();
        }

        public async Task<Client> GetClient(int clientId)
        {
            return await _context.Clients.FirstOrDefaultAsync(c => c.Id == clientId);
        }

        public async Task<Client> AddClient(Client client)
        {
            var createdClient = await _context.Clients.AddAsync(client);
            await _context.SaveChangesAsync();
            return createdClient.Entity;
        }

        public async Task<Client> UpdateClient(Client client)
        {
            var existingClient = await _context.Clients.FirstOrDefaultAsync(c => c.Id == client.Id);
            if (existingClient != null)
            {
                existingClient.Name = client.Name;
                existingClient.Email = client.Email;
                existingClient.Phone = client.Phone;
                await _context.SaveChangesAsync();
                return existingClient;
            }
            return null;
        }

        public async Task<Client> DeleteClient(int clientId)
        {
            var client = await _context.Clients.FirstOrDefaultAsync(c => c.Id == clientId);
            if (client != null)
            {
                _context.Clients.Remove(client);
                await _context.SaveChangesAsync();
                return client;
            }
            return null;
        }
    }
}
