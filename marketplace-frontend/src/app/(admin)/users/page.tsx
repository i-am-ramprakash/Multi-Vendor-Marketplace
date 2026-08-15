'use client';

import { Search, Shield, Ban, CheckCircle } from 'lucide-react';
import { formatDate, getStatusColor } from '@/lib/utils';

export default function AdminUsersPage() {
  const users = [
    { id: 1, name: 'John Doe', email: 'john@example.com', roles: ['CUSTOMER'], status: 'ACTIVE', joined: '2024-01-10' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', roles: ['VENDOR'], status: 'ACTIVE', joined: '2024-02-15' },
    { id: 3, name: 'Admin User', email: 'admin@example.com', roles: ['ADMIN'], status: 'ACTIVE', joined: '2024-01-01' },
    { id: 4, name: 'Bob Johnson', email: 'bob@example.com', roles: ['CUSTOMER'], status: 'SUSPENDED', joined: '2024-03-05' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">User Management</h1>

      <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
        <div className="flex gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search users..."
              className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>
          <select className="border rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-primary">
            <option value="">All Roles</option>
            <option value="CUSTOMER">Customer</option>
            <option value="VENDOR">Vendor</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <div className="grid grid-cols-6 gap-4 text-sm font-medium text-gray-600">
            <div>User</div>
            <div>Email</div>
            <div>Roles</div>
            <div>Status</div>
            <div>Joined</div>
            <div>Actions</div>
          </div>
        </div>
        {users.map((user) => (
          <div key={user.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-6 gap-4 items-center">
              <div className="font-medium">{user.name}</div>
              <div className="text-gray-600">{user.email}</div>
              <div className="flex gap-1">
                {user.roles.map((role) => (
                  <span key={role} className="px-2 py-1 bg-gray-100 rounded text-xs">
                    {role}
                  </span>
                ))}
              </div>
              <div>
                <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(user.status)}`}>
                  {user.status}
                </span>
              </div>
              <div className="text-gray-600">{formatDate(user.joined)}</div>
              <div className="flex space-x-2">
                <button className="p-2 hover:bg-gray-100 rounded" title="View">
                  <Shield className="h-4 w-4" />
                </button>
                {user.status === 'ACTIVE' ? (
                  <button className="p-2 hover:bg-red-100 rounded text-red-600" title="Suspend">
                    <Ban className="h-4 w-4" />
                  </button>
                ) : (
                  <button className="p-2 hover:bg-green-100 rounded text-green-600" title="Activate">
                    <CheckCircle className="h-4 w-4" />
                  </button>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
