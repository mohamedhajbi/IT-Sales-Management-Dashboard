using Models;

namespace Client_service.Repositories
{
    public interface IClientRepository
    {
        Task<IEnumerable<Client>> GetClients();
        Task<Client> GetClient(int clientId);
        Task<Client> AddClient(Client client);
        Task<Client> UpdateClient(Client client);
        Task<Client> DeleteClient(int clientId);
    }
}
