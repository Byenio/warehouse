import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
} from "@/components/ui/card";

import { Skeleton } from "@/components/ui/skeleton";

export default function Loading() {
    return (
        <Card className="max-w-[480px] my-2">
            <CardHeader className="h-24 space-y-2">
                <Skeleton className="h-5 w-1/2" />
                <Skeleton className="h-4 w-3/4" />
            </CardHeader>
            <CardContent className="h-42">
                <div className="flex justify-center my-4">
                    <Skeleton className="h-16 w-16 rounded-full" />
                </div>
                <div className="space-y-2">
                    <Skeleton className="h-4 w-1/3" />
                    <Skeleton className="h-4 w-1/3" />
                    <Skeleton className="h-4 w-1/3" />
                </div>
            </CardContent>
            <CardFooter className="flex justify-end items-end text-sm text-slate-500">
                <Skeleton className="h-4 w-20" />
            </CardFooter>
        </Card>
    );
}
