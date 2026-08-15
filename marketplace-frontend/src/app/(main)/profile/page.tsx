'use client';

import { useAuth } from '@/hooks/useAuth';
import { User, Mail, Phone, Shield } from 'lucide-react';

export default function ProfilePage() {
  const { user } = useAuth();

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold mb-8">My Profile</h1>

      <div className="bg-white rounded-lg shadow-sm p-6">
        <div className="flex items-center mb-6">
          <div className="w-20 h-20 bg-primary/10 rounded-full flex items-center justify-center">
            <User className="h-10 w-10 text-primary" />
          </div>
          <div className="ml-6">
            <h2 className="text-2xl font-bold">
              {user?.firstName} {user?.lastName}
            </h2>
            <p className="text-gray-600">{user?.email}</p>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="space-y-4">
            <h3 className="font-semibold text-lg">Personal Information</h3>
            <div className="flex items-center text-gray-600">
              <Mail className="h-5 w-5 mr-3" />
              <span>{user?.email}</span>
            </div>
            <div className="flex items-center text-gray-600">
              <Phone className="h-5 w-5 mr-3" />
              <span>{user?.phoneNumber || 'Not provided'}</span>
            </div>
            <div className="flex items-center text-gray-600">
              <Shield className="h-5 w-5 mr-3" />
              <span>{user?.roles?.join(', ')}</span>
            </div>
          </div>

          <div className="space-y-4">
            <h3 className="font-semibold text-lg">Account Settings</h3>
            <button className="w-full text-left px-4 py-3 border rounded-lg hover:bg-gray-50">
              Edit Profile
            </button>
            <button className="w-full text-left px-4 py-3 border rounded-lg hover:bg-gray-50">
              Change Password
            </button>
            <button className="w-full text-left px-4 py-3 border rounded-lg hover:bg-gray-50">
              Notification Preferences
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
