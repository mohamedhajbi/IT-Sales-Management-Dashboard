using Microsoft.EntityFrameworkCore;
using Models;

namespace Client_service
{
    public class ClientDbContext : DbContext
    {
        public ClientDbContext(DbContextOptions<ClientDbContext> options) : base(options) { }
        public DbSet<Client> Clients { get; set; }
    }

}
